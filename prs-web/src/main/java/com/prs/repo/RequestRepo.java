package com.prs.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer> {

	Request findTopByOrderByRequestNumberDesc();

	List<Request> findByAndUserIdNotAndStatus(int userId, String status);




}