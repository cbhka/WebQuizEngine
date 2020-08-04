package com.example.demo.controller;


import com.example.demo.dto.AnswerDto;
import com.example.demo.dto.QuizDto;
import com.example.demo.dto.QuizResponseDto;
import com.example.demo.entity.Answer;
import com.example.demo.entity.Quiz;
import com.example.demo.entity.QuizCompletion;
import com.example.demo.entity.User;
import com.example.demo.repository.QuizCompletionsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

	private static final QuizResponseDto CORRECT_ANSWER_RESPONSE = new QuizResponseDto(true,
			"Congratulations, you're right!");

	private static final QuizResponseDto INCORRECT_ANSWER_RESPONSE = new QuizResponseDto(false,
			"Wrong answer! Please, try again.");

	private static final int DEFAULT_PAGING_SIZE = 10;

	@Autowired
	private QuizService quizService;

	@Autowired
	private QuizCompletionsRepository quizCompletionsRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public ResponseEntity<Page<QuizDto>> getAllQuizzes(
			@RequestParam(defaultValue = "0") Integer page) {
		Page<Quiz> quizPage = quizService.getAllQuizzes(page);

		return new ResponseEntity<>(quizPage.map(this::convertQuizToDto),
				HttpStatus.OK);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
		QuizDto quiz = convertQuizToDto(quizService.getQuizById(id));
		return new ResponseEntity<>(quiz, HttpStatus.OK);
	}

	@GetMapping(path = "/completed")
	public ResponseEntity<Page<QuizCompletion>> getCompletions (
			@RequestParam(defaultValue = "0") Integer page) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		User user = userRepository.findByEmail(userDetails.getUsername())
				.orElse(null);

		if (user != null) {
			Pageable paging = PageRequest.of(page, DEFAULT_PAGING_SIZE, Sort.by("completedAt").descending());
			Page<QuizCompletion> quizCompletions = quizCompletionsRepository
					.findByUserId(user.getId(), paging);
			return new ResponseEntity<>(quizCompletions,
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping
	public ResponseEntity<QuizDto> saveQuiz(@Valid @RequestBody Quiz quiz) {
		System.out.println("QuizController: saveQuiz: quiz: " + quiz);

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		quiz.setWriter(userDetails.getUsername());

		QuizDto savedQuiz = convertQuizToDto(quizService.saveQuiz(quiz));
		return new ResponseEntity<>(savedQuiz, HttpStatus.OK);
	}

	@PostMapping(path = "/{id}/solve")
	public ResponseEntity<QuizResponseDto> solveQuiz(@PathVariable Long id, @Valid @RequestBody AnswerDto answerDto) {
		System.out.println("QuizController: solveQuiz: answers: " + answerDto.getAnswer());

		boolean isSolved = solveQuiz(id, answerDto.getAnswer());

		if (isSolved) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			userRepository.findByEmail(userDetails.getUsername())
					.ifPresent(user ->
							quizCompletionsRepository.save(
									new QuizCompletion(user.getId(),
											id,
											LocalDateTime.now().toString())
							)
					);

			return new ResponseEntity<>(CORRECT_ANSWER_RESPONSE,
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(INCORRECT_ANSWER_RESPONSE,
					HttpStatus.OK);
		}

	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
		System.out.println("QuizController: deleteQuiz: id: " + id);

		Quiz  quiz = quizService.getQuizById(id);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (quiz.getWriter().equals(userDetails.getUsername())) {
			quizService.deleteQuiz(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

	}

	@ExceptionHandler({IndexOutOfBoundsException.class, NoSuchElementException.class})
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public HashMap<String, String> handleIndexOutOfBoundsException(Exception e) {

		HashMap<String, String> response = new HashMap<>();
		response.put("message", "Id not found");
		response.put("error", e.getClass().getSimpleName());

		return response;
	}

	@ExceptionHandler({NullPointerException.class})
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public HashMap<String, String> handleNullPointerException(Exception e) {

		HashMap<String, String> response = new HashMap<>();
		response.put("message", "Bad Request");
		response.put("error", e.getClass().getSimpleName());

		return response;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class})
	public Map<String, String> handleValidationExceptions(
			MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return errors;
	}


	private boolean solveQuiz(Long quizId, @NotNull List<Answer> answers){

		Quiz quiz = quizService.getQuizById(quizId);
		List<Integer> correctAnswers = getAnswersAsNumbers(quiz.getAnswer());
		List<Integer> providedAnswers = getAnswersAsNumbers(answers);

		System.out.println("QuizServiceImpl: solveQuiz: correctAnswers: " + correctAnswers);
		System.out.println("QuizServiceImpl: solveQuiz: quiz.options: " + quiz.getOptions());
		System.out.println("QuizServiceImpl: solveQuiz: providedAnswers: " + providedAnswers);

		Collections.sort(correctAnswers);
		Collections.sort(providedAnswers);
		return correctAnswers.equals(providedAnswers);

	}

	private QuizDto convertQuizToDto(Quiz quiz) {
		List<String> optionsAsString = new ArrayList<>();
		quiz.getOptions().forEach(op -> optionsAsString.add(op.getOption()));
		return new QuizDto(quiz.getId(), quiz.getTitle(), quiz.getText(), optionsAsString);
	}

	private List<QuizDto> convertQuizzesToDto(List<Quiz> quizzes) {

		List<QuizDto> quizDtoList = new ArrayList<>();
		for (Quiz quiz : quizzes) {
			quizDtoList.add(convertQuizToDto(quiz));
		}

		return quizDtoList;
	}

	private List<Integer> getAnswersAsNumbers(List<Answer> answerList) {
		List<Integer> answersAsNumbers = new ArrayList<>();
		answerList.forEach(an -> answersAsNumbers.add(an.getNumber()));
		return answersAsNumbers;
	}

}
