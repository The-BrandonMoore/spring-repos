
package com.movie.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.movie.model.Actor;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
	private static List<Actor> actors = new ArrayList<>();

	@GetMapping("/")
	public List<Actor> getAll() {
		return actors;
	}

	@GetMapping("/{id}")
	public Actor get(@PathVariable int id) {
		id = id-1;
		return actors.get(id);
	}

	@PostMapping("")
	public Actor add(@RequestBody Actor actor) {
		int newId = 0;
		for (Actor a : actors) {
			newId = Math.max(newId, a.getId());}
		newId++;
		actor.setId(newId);
		actors.add(actor);
		return actor;
	}

	@PutMapping("/{id}/")
	public Actor update(@PathVariable int id, @RequestBody Actor actor) {
		
		int idx = -1;
		for (int i = 0; i < actors.size(); i++) {
			if (actors.get(i).getId() == actor.getId()) {idx = i;
			actors.set(i, actor);
			break;
			}
				
		}
		if (idx < 0) {
			System.err.println("Error updating actor - id not found");
		}
		return actor;
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int id) {
		for (Actor a: actors) {
			if (a.getId() == id) {
				actors.remove(a);
				break;
			}
		}
		return "actor removed";
	}
}
