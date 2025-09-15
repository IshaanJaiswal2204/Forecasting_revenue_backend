package com.revenueforecast.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.revenueforecast.model.Baseline;

public interface BaselineRepository extends JpaRepository<Baseline, Integer> {
    Page<Baseline> findAll(Pageable pageable);
}