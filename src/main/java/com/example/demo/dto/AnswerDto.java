package com.example.demo.dto;

import com.example.demo.entity.Answer;

import javax.validation.constraints.NotNull;
import java.util.List;


/***
 * Answer Dto for when Client sends an answer to quiz
 */
public class AnswerDto {

	@NotNull
	private List<Answer> answer;

	public AnswerDto() {
	}

	public AnswerDto(List<Answer> answer) {
		this.answer = answer;
	}

	public List<Answer> getAnswer() {
		return answer;
	}

	public void setAnswer(List<Answer> answer) {
		this.answer = answer;
	}
}
