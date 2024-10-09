package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.model.LineItem;
import com.prs.repo.LineItemRepo;

@RestController
@RequestMapping("/api/lineitems")
public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;

	@GetMapping("/")
	public List<LineItem> getAll() {
		return lineItemRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<LineItem> getById(@PathVariable int id) {
		Optional<LineItem> i = lineItemRepo.findById(id);
		if (i.isPresent()) {
			return i;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
		}

	}

	@PostMapping
	public LineItem add(@RequestBody LineItem lineItem) {
		return lineItemRepo.save(lineItem);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void put(@PathVariable int id, @RequestBody LineItem lineItem) {
		if (id != lineItem.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem id mismatch vs URL.");
		}
		else if (lineItemRepo.existsById(lineItem.getId())) {
			lineItemRepo.save(lineItem);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem not found for id: " + id);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		if (lineItemRepo.existsById(id)) {
			lineItemRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem not found for id: " + id);
		}
	}
}
