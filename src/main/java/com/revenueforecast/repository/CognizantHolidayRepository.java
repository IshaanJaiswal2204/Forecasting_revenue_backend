package com.revenueforecast.repository;

import com.revenueforecast.model.CognizantHoliday;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CognizantHolidayRepository extends CrudRepository<CognizantHoliday, String> {
	
}
