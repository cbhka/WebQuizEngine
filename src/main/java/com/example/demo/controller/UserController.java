package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
		System.out.println("UserController: registerUser: UserDto: " + userDto);

		String userEmail = userDto.getEmail();
		System.out.println("UserController: registerUser: Email: " + userEmail);
		User user = userRepository.findByEmail(userEmail).orElse(null);
		if (user == null) {
			userRepository.save(
					new User(userEmail,
							userDto.getPassword(),
							true,
							"ROLE_USER")
			);
			return new ResponseEntity<>("registration has been completed successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Email already taken", HttpStatus.BAD_REQUEST);
		}

	}
}