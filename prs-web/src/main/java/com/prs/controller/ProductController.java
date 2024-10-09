package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.model.Product;
import com.prs.repo.ProductRepo;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepo productRepo;

	@GetMapping("/")
	public List<Product> getAll() {
		return productRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Product> getById(@PathVariable int id) {
		Optional<Product> p = productRepo.findById(id);
		if (p.isPresent()) {
			return p;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
		}

	}

	@PostMapping
	public Product add(@RequestBody Product product) {
		return productRepo.save(product);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void put(@PathVariable int id, @RequestBody Product product) {
		if (id != product.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id mismatch vs URL.");
		}
		else if (productRepo.existsById(product.getId())) {
			productRepo.save(product);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found for id: " + id);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		if (productRepo.existsById(id)) {
			productRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found for id: " + id);
		}
	}
}