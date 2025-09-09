package com.revenueforecast.repository;

import com.revenueforecast.model.Baseline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaselineRepository extends JpaRepository<Baseline, Long> {
}
