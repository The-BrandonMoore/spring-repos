package com.movie.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.movie.model.Movie;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private static List<Movie> movies = new ArrayList<>();

	@GetMapping("/")
	public List<Movie> getAll() {
		return movies;
	}

	@GetMapping("/{idx}")
	public Movie get(@PathVariable int idx) {
		return movies.get(idx);
	}

	@PostMapping("")
	public String add(Movie movie) {
		if (movies.indexOf(movie) >= 0) {
			return "Movie already existst";
		} else {
			movies.add(movie);
			return "Movie added.";
		}
	}

	@PutMapping("/{idx}/{movie}")
	public String update(@PathVariable int idx, @PathVariable Movie movie) {
		movies.set(idx, movie);
		return "Hobby updated.";
	}

	@DeleteMapping("/{idx}")
	public String delete(@PathVariable int idx) {
		movies.remove(idx);
		return "Movie removed";
	}
}
