package com.example.demo.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Boolean enabled;

	@Column(nullable = false)
	private String roles;

	@OneToMany(targetEntity = QuizCompletion.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "quizCom_fr", referencedColumnName = "id")
	private List<QuizCompletion> quizCompletions = new ArrayList<>();

	public User() {
	}

	public User(String email, String password, Boolean enabled, String roles) {
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public List<QuizCompletion> getQuizCompletions() {
		return quizCompletions;
	}

	public void setQuizCompletions(List<QuizCompletion> quizCompletions) {
		this.quizCompletions = quizCompletions;
	}
}