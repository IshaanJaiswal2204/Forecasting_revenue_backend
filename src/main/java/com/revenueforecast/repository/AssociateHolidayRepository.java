package com.revenueforecast.repository;

import com.revenueforecast.model.AssociateHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AssociateHolidayRepository extends CrudRepository<AssociateHoliday, Integer> { 
	
}
