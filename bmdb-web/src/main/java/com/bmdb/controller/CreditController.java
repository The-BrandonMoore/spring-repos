package com.bmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.db.CreditRepo;
import com.bmdb.model.Credit;

@RestController
@RequestMapping("/api/credits")
public class CreditController {
	
	@Autowired
	private CreditRepo creditRepo;

	@GetMapping("/")
	public List<Credit> getAll() {
		return creditRepo.findAll();
	}
	@GetMapping("/{id}")
	public Optional<Credit> getById(@PathVariable int id) {
		Optional<Credit> c = creditRepo.findById(id);
		if (c.isPresent()) {
			return c;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
		}
	}

	@PostMapping("")
	public Credit add(@RequestBody Credit credit) {
		
		return creditRepo.save(credit);
	}
	@PutMapping("/{id}")
	public Credit put(@PathVariable int id, @RequestBody Credit credit) {
		Credit c = null;
		if (id != credit.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit id mismatch vs URL.");
		}
		else if (creditRepo.existsById(credit.getId())) {
			c = creditRepo.save(credit);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit not found for id: " + id);
		}
		return c;
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		if (creditRepo.existsById(id)) {
			creditRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit not found for id: " + id);
		}
	}
	
	//BEYOND CRUD BELOW HERE
	
	//return all credits for a particular movie id -- Movie-Credits
	@GetMapping("/movie-credits/{movieId}")
	public List<Credit> getCreditsForMovie(@PathVariable int movieId){
		return creditRepo.findMovieById(movieId);
	}
	
	//return all credits for a particular actor -- Actor-Filmography
	@GetMapping("/actor-credits/{actorId}")
	public List<Credit> getCreditsForActor(@PathVariable int actorId){
		return creditRepo.findActorById(actorId);
	}
}
