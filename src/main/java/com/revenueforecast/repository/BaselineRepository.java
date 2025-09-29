//package com.revenueforecast.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.revenueforecast.model.Baseline;
//
//public interface BaselineRepository extends JpaRepository<Baseline, Integer> {
//    Page<Baseline> findAll(Pageable pageable);
//}

package com.revenueforecast.repository;

import com.revenueforecast.model.Baseline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BaselineRepository extends JpaRepository<Baseline, Integer> {

    // For Baseline view: active during the selected month
    Page<Baseline> findByCurrentStartDateLessThanEqualAndCurrentEndDateGreaterThanEqual(
        LocalDate end, LocalDate start, Pageable pageable
    );

    // For MostLikely view: predicted to be active during the selected month
    Page<Baseline> findByMostLikelyStartDateLessThanEqualAndMostLikelyEndDateGreaterThanEqual(
        LocalDate end, LocalDate start, Pageable pageable
    );
}
