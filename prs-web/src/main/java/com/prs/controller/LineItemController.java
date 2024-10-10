package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.model.*;
import com.prs.repo.*;

@RestController
@RequestMapping("/api/lineitems")
public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo requestRepo;
	@Autowired
	private ProductRepo productRepo;

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
	    if (lineItem.getRequest() == null || lineItem.getRequest().getId() == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request must not be null and must have a valid ID");
	    }
	    if (lineItem.getProduct() == null || lineItem.getProduct().getId() == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product must not be null and must have a valid ID");
	    }
		
		//get request
		Request request = requestRepo.findById(lineItem.getRequest().getId())
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

		//get product
		 Product product = productRepo.findById(lineItem.getProduct().getId())
			        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

		lineItem.setRequest(request);
		lineItem.setProduct(product);
		lineItemRepo.save(lineItem);
		request.getLineItems().add(lineItem);
		//new double total variable 
		double total = 0;
		//recalculate the total of the request
		for (LineItem item: request.getLineItems()) {
			total += (item.getProduct().getPrice()) * (item.getQuantity());
		}
		request.setTotal(total);
		requestRepo.save(request);
		return lineItem;
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void put(@PathVariable int id, @RequestBody LineItem lineItem) {
		if (id != lineItem.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem id mismatch vs URL.");
		}
		else if (lineItemRepo.existsById(lineItem.getId())) {
			//find request that matches the lineItemId
			Request request = requestRepo.findById(lineItem.getRequest().getId())
			        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

			//get product
			 Product product = productRepo.findById(lineItem.getProduct().getId())
				        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

			lineItem.setRequest(request);
			lineItem.setProduct(product);
			lineItemRepo.save(lineItem);
			//I'm not sure if I need to do something to the request here or not. I'm going to try not.
			//new double total variable 
			double total = 0;
			//recalculate the total of the request
			for (LineItem item: request.getLineItems()) {
				total += (item.getProduct().getPrice()) * (item.getQuantity());
			}
			request.setTotal(total);
			requestRepo.save(request);	
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem not found for id: " + id);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		if (lineItemRepo.existsById(id)) {
			Optional<LineItem> findLineItem = lineItemRepo.findById(id);
			if (!findLineItem.isPresent()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem not found for id: " + id);
			}
			LineItem lineItem = findLineItem.get();
			Request request = requestRepo.findById(lineItem.getRequest().getId())
			        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

			lineItem.setRequest(request);
			lineItemRepo.deleteById(id);
			request.getLineItems().remove(lineItem);
			requestRepo.save(request);
			//new double total variable 
			double total = 0;
			//recalculate the total of the request
			for (var item: request.getLineItems()) {
				total += item.getProduct().getPrice() * item.getQuantity();
			}
			request.setTotal(total);
			requestRepo.save(request);	
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem not found for id: " + id);
		}
	}
	
	@GetMapping("lines-for-req/{requestId}")
	public List<LineItem> getLineItemsForRequestId(int requestId){
		List<LineItem> lineItems = lineItemRepo.findByRequestIdEquals(requestId);
		if (lineItems == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LineItem not found for Request: " + requestId);
		}
		return lineItems;
	}
	
	
	
	
}
