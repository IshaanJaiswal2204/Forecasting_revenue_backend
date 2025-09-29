package com.revenueforecast.repository;

import com.revenueforecast.model.CognizantHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
public interface CognizantHolidayRepository extends JpaRepository<CognizantHoliday, Long> {

    List<CognizantHoliday> findByLocationAndDateBetween(String location, LocalDate start, LocalDate end);

    List<CognizantHoliday> findByDate(LocalDate date);

    List<CognizantHoliday> findByLocation(String location);
}
