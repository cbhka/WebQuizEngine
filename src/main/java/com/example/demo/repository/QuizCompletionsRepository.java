package com.example.demo.repository;

import com.example.demo.entity.QuizCompletion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizCompletionsRepository extends PagingAndSortingRepository<QuizCompletion, Long> {

	Page<QuizCompletion> findByUserId(Long userId, Pageable pageable);
}
