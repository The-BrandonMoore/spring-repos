package com.prs.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

@Entity
public class LineItem implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name="requestId")
	@JsonIgnoreProperties("request")
	private Request request;
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	private Integer quantity;
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	
	
}
