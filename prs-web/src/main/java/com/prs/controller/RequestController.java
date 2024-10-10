package com.prs.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.model.*;

import com.prs.repo.RequestRepo;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

	@Autowired
	private RequestRepo requestRepo;

	@GetMapping("/")
	public List<Request> getAll() {
		return requestRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Request> getById(@PathVariable int id) {
		Optional<Request> r = requestRepo.findById(id);
		if (r.isPresent()) {
			return r;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
		}

	}

	@PostMapping
	public Request add(@RequestBody RequestForm requestForm) {
		// need to do logic for request number creation. Must have a first request in DB
		// with correct format
		Request request = new Request();
		request.setDescription(requestForm.getDescription());
		request.setUser(requestForm.getUser());
		request.setDeliveryMode(requestForm.getDeliveryMode());
		request.setDateNeeded(requestForm.getDateNeeded());
		request.setJustification(requestForm.getJustification());
		request.setStatus("NEW");
		request.setTotal(0);
		request.setSubmittedDate(LocalDateTime.now());
		// Makes the new request number
		Request lastRequest = requestRepo.findTopByOrderByRequestNumberDesc();
		String lastRequestNumber = lastRequest.getRequestNumber();
		int lastFour = Integer.parseInt(lastRequestNumber.substring(7)) + 1;
		String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String newRequestNumber = new StringBuilder().append("R").append(dateStr)
				.append(String.format("%04d", lastFour)).toString();
		request.setRequestNumber(newRequestNumber);

		return requestRepo.save(request);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void put(@PathVariable int id, @RequestBody Request request) {
		if (id != request.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request id mismatch vs URL.");
		} else if (requestRepo.existsById(request.getId())) {
			requestRepo.save(request);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found for id: " + id);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		if (requestRepo.existsById(id)) {
			requestRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found for id: " + id);
		}
	}
	@PutMapping("/submit-review/{id}")
	public Request submitReview(@PathVariable Integer id) {
	    Optional<Request> findRequest = requestRepo.findById(id);
	    if (!findRequest.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found for id: " + id);
	    }
	    Request request = findRequest.get();
	    double total = request.getTotal();
	    if (total <= 50) {
	        request.setStatus("APPROVED");
	    } else {
	        request.setStatus("REVIEW");
	    }
	    request.setSubmittedDate(LocalDateTime.now());
	    requestRepo.save(request);

	    return request;
	}


	@GetMapping("/list-review/{userId}")
	public List<Request> getAll(@PathVariable int userId) {
		return requestRepo.findByAndUserIdNotAndStatus(userId, "REVIEW");
	}

	@PutMapping("approve/{id}")
	public Request requestApproved(@PathVariable int id) {
		Optional<Request> request1 = requestRepo.findById(id);
	    if (!request1.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found for id: " + id);
	    }
	    Request request = request1.get();
	    request.setStatus("APPROVED");
	    requestRepo.save(request);
	    return request;
	}
	
	@PutMapping("reject/{id}")
	public Request requestDenied(@PathVariable int id, @RequestBody String reasonForRejection) {
		Optional<Request> request1 = requestRepo.findById(id);
	    if (!request1.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request not found for id: " + id);
	    }
	    Request request = request1.get();
	    request.setStatus("REJECTED");
	    request.setReasonForRejection(reasonForRejection);
	    requestRepo.save(request);
	    return request;
	}
}
