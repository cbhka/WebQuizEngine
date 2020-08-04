package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
public class Quiz {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Column(nullable = false)
	@NotBlank
	private String title;

	@Column(nullable = false)
	@NotBlank
	private String text;

	@Column
	@JsonIgnore
	private String writer;

	@OneToMany(targetEntity = Option.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "oq_fr", referencedColumnName = "id")
	@Size(min = 2)
	@NotNull
	private List<Option> options;

	@OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "aq_fr", referencedColumnName = "id")
	private List<Answer> answer =  new ArrayList<>();

	public Quiz() {
	}

	public Quiz(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public List<Answer> getAnswer() {
		return answer;
	}

	public void setAnswer(List<Answer> answer) {
		this.answer = answer;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}
}
