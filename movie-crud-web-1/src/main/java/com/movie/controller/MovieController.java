
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

	@GetMapping("/{id}")
	public Movie get(@PathVariable int id) {
		id = id-1;
		return movies.get(id);
	}

	@PostMapping("")
	public Movie add(@RequestBody Movie movie) {
		int newId = 0;
		for (Movie m : movies) {
			newId = Math.max(newId, m.getId());}
		newId++;
		movie.id = newId;
		movies.add(movie);
		return movie;
	}

	@PutMapping("/{id}/")
	public Movie update(@PathVariable int id, @RequestBody Movie movie) {
		
		int idx = -1;
		for (int i = 0; i < movies.size(); i++) {
			if (movies.get(i).getId() == movie.getId()) {idx = i;
			movies.set(i, movie);
			break;
			}
				
		}
		if (idx < 0) {
			System.err.println("Errorupdating movie - id not found");
		}
		return movie;
	}

	@DeleteMapping("/{idx}")
	public String delete(@PathVariable int id) {
		for (Movie m:movies) {
			if (m.getId() == id) {
				movies.remove(m);
				break;
			}
		}
		return "movie removed";
	}
}
