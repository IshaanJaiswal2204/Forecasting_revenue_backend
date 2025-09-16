package com.revenueforecast.service;

import com.revenueforecast.dto.MostLikelyResponseDTO;
import com.revenueforecast.model.AssociateHoliday;
import com.revenueforecast.model.CognizantHoliday;
import com.revenueforecast.model.MostLikely;
import com.revenueforecast.repository.AssociateHolidayRepository;
import com.revenueforecast.repository.CognizantHolidayRepository;
import com.revenueforecast.repository.MostLikelyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MostLikelyService {

    private static final Logger logger = LoggerFactory.getLogger(MostLikelyService.class);

    @Autowired private MostLikelyRepository mostLikelyRepo;
    @Autowired private AssociateHolidayRepository associateHolidayRepo;
    @Autowired private CognizantHolidayRepository cognizantHolidayRepo;

    public Page<MostLikelyResponseDTO> getMostLikelyByMonth(int month, int year, int page, int size) {
        long startTime = System.currentTimeMillis();
        logger.info("Calculating MostLikely for year={}, month={}, page={}, size={}", year, month, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<MostLikely> pageData = mostLikelyRepo.findAll(pageable);
        logger.debug("Fetched {} MostLikely records from page {}", pageData.getNumberOfElements(), page);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<MostLikelyResponseDTO> dtoList = pageData.stream()
            .filter(m -> {
                LocalDate start = m.getCurrentStartDate();
                LocalDate end = m.getCurrentEndDate();
                boolean isActive = start != null && end != null && !(end.isBefore(firstDay) || start.isAfter(lastDay));
                if (!isActive) {
                    logger.debug("Skipping associate {} due to inactive date range", m.getAssociateId());
                }
                return isActive;
            })
            .map(m -> {
                logger.info("Processing associate: {} - {}", m.getAssociateId(), m.getAssociateName());

                MostLikelyResponseDTO dto = new MostLikelyResponseDTO();
                dto.setAssociateId(m.getAssociateId());
                dto.setName(m.getAssociateName());
                dto.setServiceLine(m.getSl());
                dto.setProjectId(m.getProjectId());
                dto.setProjectName(m.getProjectDescription());
                dto.setCurrentStart(m.getCurrentStartDate());
                dto.setCurrentEnd(m.getCurrentEndDate());
                dto.setBillability(m.getBillabilityStatus());
                dto.setCity(m.getCity());
                dto.setRate(m.getBillRate());
                dto.setConfidencePercent(m.getPercentAllocation());

                double hours = m.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
                dto.setHours(hours);

                // Active period within the month
                LocalDate activeStart = m.getCurrentStartDate().isBefore(firstDay) ? firstDay : m.getCurrentStartDate();
                LocalDate activeEnd = m.getCurrentEndDate().isAfter(lastDay) ? lastDay : m.getCurrentEndDate();
                int activeDays = getWorkingDaysExcludingWeekends(activeStart, activeEnd);

                int associateHolidays = getAssociateHolidayDays(m.getAssociateId(), month);
                int cognizantHolidays = getCognizantHolidayDays(m.getCity(), month);
                int finalWorkingDays = Math.max(0, activeDays - associateHolidays - cognizantHolidays);
                double monthHours = finalWorkingDays * hours;
                double rate = m.getBillRate() != null ? m.getBillRate() : 0;
                double monthRevenue = rate * monthHours;

                // Previous month
                YearMonth previous = YearMonth.of(year, month).minusMonths(1);
                LocalDate prevFirstDay = previous.atDay(1);
                LocalDate prevLastDay = previous.atEndOfMonth();
                LocalDate prevActiveStart = m.getCurrentStartDate().isBefore(prevFirstDay) ? prevFirstDay : m.getCurrentStartDate();
                LocalDate prevActiveEnd = m.getCurrentEndDate().isAfter(prevLastDay) ? prevLastDay : m.getCurrentEndDate();
                int prevActiveDays = getWorkingDaysExcludingWeekends(prevActiveStart, prevActiveEnd);

                int prevAssociateHolidays = getAssociateHolidayDays(m.getAssociateId(), previous.getMonthValue());
                int prevCognizantHolidays = getCognizantHolidayDays(m.getCity(), previous.getMonthValue());
                int finalPrevWorkingDays = Math.max(0, prevActiveDays - prevAssociateHolidays - prevCognizantHolidays);
                double previousMonthHours = finalPrevWorkingDays * hours;
                double previousRevenue = rate * previousMonthHours;

                double variance = monthRevenue - previousRevenue;

                dto.setMonthHours(monthHours);
                dto.setMonthRevenue(monthRevenue);
                dto.setPreviousRevenue(previousRevenue);
                dto.setVariance(variance);

                return dto;
            })
            .collect(Collectors.toList());

        logger.info("Completed MostLikely calculation in {} ms", System.currentTimeMillis() - startTime);
        return new PageImpl<>(dtoList, pageable, pageData.getTotalElements());
    }

    private int getWorkingDaysExcludingWeekends(LocalDate start, LocalDate end) {
        int workingDays = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }
        logger.debug("Working days between {} and {}: {}", start, end, workingDays);
        return workingDays;
    }

    private int getAssociateHolidayDays(Integer associateId, int month) {
        return associateHolidayRepo.findById(associateId)
            .map(h -> getMonthValueFromAssociate(h, month))
            .orElseGet(() -> {
                logger.warn("No associate holiday found for ID: {}", associateId);
                return 0;
            });
    }

    private int getCognizantHolidayDays(String location, int month) {
        return cognizantHolidayRepo.findById(location)
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
