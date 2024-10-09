package com.prs.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.LineItem;

public interface LineItemRepo extends JpaRepository<LineItem, Integer> {


}