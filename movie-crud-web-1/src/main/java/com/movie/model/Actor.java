package com.movie.model;

import java.sql.Date;
import java.time.LocalDate;

public class Actor {
	
	public int id;
	public String firstName;
	public String lastName;
	public String gender;
	public LocalDate birthdate;

	
	
	public Actor(int id, String firstName, String lastName, String gender, LocalDate birthdate) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthdate = birthdate;
	}
	public Actor() {
		super();
	}
	


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirtdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
}
