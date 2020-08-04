package com.example.demo.service;

import com.example.demo.entity.Quiz;
import org.springframework.data.domain.Page;

public interface QuizService {

	Page<Quiz> getAllQuizzes(int pageNumber);

	Quiz getQuizById(Long id);

	Quiz saveQuiz(Quiz quiz);

	void deleteQuiz(Long id);

}
