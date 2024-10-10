package com.bmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.db.MovieRepo;
import com.bmdb.model.Movie;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

	@Autowired
	private MovieRepo movieRepo;

	@GetMapping("/")
	public List<Movie> getAll() {
		return movieRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Movie> getById(@PathVariable int id) {
		Optional<Movie> m = movieRepo.findById(id);
		if (m.isPresent()) {
			return m;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
		}

	}

	@PostMapping
	public Movie add(@RequestBody Movie movie) {
		return movieRepo.save(movie);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void put(@PathVariable int id, @RequestBody Movie movie) {
		if (id != movie.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie id mismatch vs URL.");
		}
		else if (movieRepo.existsById(movie.getId())) {
			movieRepo.save(movie);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie not found for id: " + id);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		if (movieRepo.existsById(id)) {
			movieRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie not found for id: " + id);
		}
	}
	
	@GetMapping("/year-and-rating/{rating}/{year}")
	public List<Movie> getMovieByYearAndRating(@PathVariable int year, @PathVariable String rating){
		return movieRepo.findByRatingAndYear(rating, year);
	}
	
	@GetMapping("/rating-after-year/{rating}/{year}")
	public List<Movie> getMovieByRatingAfterYear(@PathVariable String rating, @PathVariable int year){
		return movieRepo.findByRatingAndYearIsGreaterThanEqual(rating, year);
	}
	
	@GetMapping("/notrating/{rating}")
	public List<Movie> getMovieWhereNotRating(@PathVariable String rating){
		return movieRepo.findByRatingNot(rating);
	}
	
}
