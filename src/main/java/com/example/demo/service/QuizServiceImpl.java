package com.example.demo.service;

import com.example.demo.entity.Quiz;
import com.example.demo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class QuizServiceImpl implements QuizService {

	private static final int DEFAULT_PAGING_SIZE = 10;

	@Autowired
	private QuizRepository quizRepository;

	@Override
	public Page<Quiz> getAllQuizzes(int pageNumber) {

		Pageable paging = PageRequest.of(pageNumber, DEFAULT_PAGING_SIZE);
		return quizRepository.findAll(paging);
	}

	@Override
	public Quiz getQuizById(Long id) {
		System.out.println("QuizServiceImpl: getQuizById: quiz: " + quizRepository.findById(id));
		return quizRepository.findById(id).orElseThrow();
	}

	@Override
	public Quiz saveQuiz(Quiz quiz) {
		System.out.println("QuizServerImpl saveQuiz: quiz: " + quiz);
		return quizRepository.save(quiz);
	}

	@Override
	public void deleteQuiz(Long id) {
		quizRepository.deleteById(id);
	}

}
