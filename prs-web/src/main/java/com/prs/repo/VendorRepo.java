package com.prs.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Vendor;

public interface VendorRepo extends JpaRepository<Vendor, Integer> {


}