package com.example.demo.dto;

import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
public class UserDto {

	@NotBlank
	@Email(regexp = "(\\w+)@(\\w+)\\.(\\w+)")
	private String email;

	@NotBlank
	@Size(min = 5)
	private String password;

	public UserDto() {
	}

	public UserDto(@NotBlank @Email String email, @NotBlank String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
