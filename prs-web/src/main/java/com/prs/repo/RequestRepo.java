package com.prs.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer> {


}