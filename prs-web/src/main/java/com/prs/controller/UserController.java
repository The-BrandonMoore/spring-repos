package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.model.User;
import com.prs.model.UserLogin;
import com.prs.repo.UserRepo;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public List<User> getAll() {
		return userRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<User> getById(@PathVariable int id) {
		Optional<User> u = userRepo.findById(id);
		if (u.isPresent()) {
			return u;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
		}

	}

	@PostMapping
	public User add(@RequestBody User user) {
		return userRepo.save(user);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void put(@PathVariable int id, @RequestBody User user) {
		if (id != user.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id mismatch vs URL.");
		}
		else if (userRepo.existsById(user.getId())) {
			userRepo.save(user);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found for id: " + id);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		if (userRepo.existsById(id)) {
			userRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found for id: " + id);
		}
	}
	
	@PostMapping("/login")
	public User login(@RequestBody UserLogin userLogin) {
		User currentUser = userRepo.findByUsernameAndPassword(userLogin.getUsername(), userLogin.getPassword());
		if (currentUser != null)
		return currentUser;
		else{	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");}
	}
}
