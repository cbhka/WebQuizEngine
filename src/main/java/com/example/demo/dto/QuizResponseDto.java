package com.example.demo.dto;

/**
 * A Quiz Response DTO to send feedback on Client Answer to a Quiz
 */
public class QuizResponseDto {

	private boolean success;
	private String feedback;

	public QuizResponseDto(boolean success, String feedback) {
		this.success = success;
		this.feedback = feedback;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
