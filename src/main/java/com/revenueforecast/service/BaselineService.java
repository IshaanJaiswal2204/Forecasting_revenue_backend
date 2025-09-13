package com.revenueforecast.service;

import com.revenueforecast.dto.BaselineResponseDTO;
import com.revenueforecast.model.AssociateHoliday;
import com.revenueforecast.model.Baseline;
import com.revenueforecast.model.CognizantHoliday;
import com.revenueforecast.repository.AssociateHolidayRepository;
import com.revenueforecast.repository.BaselineRepository;
import com.revenueforecast.repository.CognizantHolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaselineService {

    private static final Logger logger = LoggerFactory.getLogger(BaselineService.class);

    @Autowired
    private BaselineRepository baselineRepository;

    @Autowired
    private AssociateHolidayRepository associateHolidayRepository;

    @Autowired
    private CognizantHolidayRepository cognizantHolidayRepository;

    public List<BaselineResponseDTO> getBaselineByMonth(int month, int year) {
        logger.info("Calculating baseline for year: {}, month: {}", year, month);

        List<Baseline> allRecords = baselineRepository.findAll();
        logger.debug("Fetched {} baseline records", allRecords.size());

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        return allRecords.stream()
            .filter(b -> {
                LocalDate start = b.getCurrentStartDate();
                LocalDate end = b.getCurrentEndDate();
                boolean isActive = (start != null && end != null) &&
                                   (!start.isAfter(lastDay) && !end.isBefore(firstDay));
                if (!isActive) {
                    logger.debug("Skipping associate {} due to inactive date range", b.getAssociateId());
                }
                return isActive;
            })
            .map(b -> {
                logger.info("Processing associate: {} - {}", b.getAssociateId(), b.getAssociateName());

                BaselineResponseDTO dto = new BaselineResponseDTO();
                dto.setAssociateId(b.getAssociateId());
                dto.setName(b.getAssociateName());
                dto.setServiceLine(b.getSl());
                dto.setProjectId(b.getProjectId());
                dto.setProjectName(b.getProjectDescription());
                dto.setCurrentStart(b.getCurrentStartDate());
                dto.setCurrentEnd(b.getCurrentEndDate());
                dto.setBillability(b.getBillabilityStatus());
                dto.setCity(b.getCity());
                dto.setRate(b.getBillRate());

                double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
                dto.setHours(hours);
                logger.debug("Assigned hours for {}: {}", b.getCountry(), hours);

                // Current month
                int workingDays = getWorkingDaysExcludingWeekends(year, month);
                int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
                int cognizantHolidays = getCognizantHolidayDays(b.getCity(), month);
                int finalWorkingDays = workingDays - associateHolidays - cognizantHolidays;
                double monthHours = finalWorkingDays * hours;
                double rate = b.getBillRate() != null ? b.getBillRate() : 0;
                double monthRevenue = rate * monthHours;

                logger.debug("MonthHours: {}, MonthRevenue: {}", monthHours, monthRevenue);

                // Previous month
                YearMonth current = YearMonth.of(year, month);
                YearMonth previous = current.minusMonths(1);
                int prevYear = previous.getYear();
                int prevMonth = previous.getMonthValue();
                int prevWorkingDays = getWorkingDaysExcludingWeekends(prevYear, prevMonth);
                int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), prevMonth);
                int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), prevMonth);
                int finalPrevWorkingDays = prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays;
                double previousMonthHours = finalPrevWorkingDays * hours;
                double previousRevenue = rate * previousMonthHours;

                logger.debug("PreviousMonthHours: {}, PreviousRevenue: {}", previousMonthHours, previousRevenue);

                double variance = monthRevenue - previousRevenue;
                logger.info("Variance for associate {}: {}", b.getAssociateId(), variance);

                dto.setMonthHours(monthHours);
                dto.setMonthRevenue(monthRevenue);
                dto.setPreviousRevenue(previousRevenue);
                dto.setVariance(variance);

                return dto;
            })
            .collect(Collectors.toList());
    }

    private int getWorkingDaysExcludingWeekends(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int workingDays = 0;
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }
        logger.debug("Working days (excluding weekends) for {}/{}: {}", month, year, workingDays);
        return workingDays;
    }

    private int getAssociateHolidayDays(Integer associateId, int month) {
        return associateHolidayRepository.findById(associateId)
            .map(h -> getMonthValueFromAssociate(h, month))
            .orElseGet(() -> {
                logger.warn("No associate holiday found for ID: {}", associateId);
                return 0;
            });
    }

    private int getCognizantHolidayDays(String location, int month) {
        return cognizantHolidayRepository.findById(location)
            .map(h -> getMonthValueFromCognizant(h, month))
            .orElseGet(() -> {
                logger.warn("No Cognizant holiday found for location: {}", location);
                return 0;
            });
    }

    private int getMonthValueFromAssociate(AssociateHoliday holiday, int month) {
        return switch (month) {
            case 1 -> holiday.getJan();
            case 2 -> holiday.getFeb();
            case 3 -> holiday.getMar();
            case 4 -> holiday.getApr();
            case 5 -> holiday.getMay();
            case 6 -> holiday.getJun();
            case 7 -> holiday.getJul();
            case 8 -> holiday.getAug();
            case 9 -> holiday.getSep();
            case 10 -> holiday.getOct();
            case 11 -> holiday.getNov();
            case 12 -> holiday.getDec();
            default -> 0;
        };
    }

    private int getMonthValueFromCognizant(CognizantHoliday holiday, int month) {
        return switch (month) {
            case 1 -> holiday.getJan();
            case 2 -> holiday.getFeb();
            case 3 -> holiday.getMar();
            case 4 -> holiday.getApr();
            case 5 -> holiday.getMay();
            case 6 -> holiday.getJun();
            case 7 -> holiday.getJul();
            case 8 -> holiday.getAug();
            case 9 -> holiday.getSep();
            case 10 -> holiday.getOct();
            case 11 -> holiday.getNov();
            case 12 -> holiday.getDec();
            default -> 0;
        };
    }
}
