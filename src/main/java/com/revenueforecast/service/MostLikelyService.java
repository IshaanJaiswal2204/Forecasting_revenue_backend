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

    @Autowired
    private MostLikelyRepository mostLikelyRepository;

    @Autowired
    private AssociateHolidayRepository associateHolidayRepository;

    @Autowired
    private CognizantHolidayRepository cognizantHolidayRepository;

    public Page<MostLikelyResponseDTO> getMostLikelyByMonth(int month, int year, int page, int size) {
        long startTime = System.currentTimeMillis();
        logger.info("Calculating MostLikely for year: {}, month: {}, page: {}, size: {}", year, month, page, size);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<MostLikely> allRecords = mostLikelyRepository.findAll();
        logger.debug("Fetched total {} MostLikely records", allRecords.size());

        List<MostLikelyResponseDTO> filteredList = allRecords.stream()
            .filter(m -> {
                LocalDate start = m.getCurrentStartDate();
                LocalDate end = m.getCurrentEndDate();
                boolean isActive = (start != null && end != null) &&
                                   (!start.isAfter(lastDay) && !end.isBefore(firstDay));
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

                int workingDays = getWorkingDaysExcludingWeekends(year, month);
                int associateHolidays = getAssociateHolidayDays(m.getAssociateId(), month);
                int cognizantHolidays = getCognizantHolidayDays(m.getCity(), month);
                int finalWorkingDays = workingDays - associateHolidays - cognizantHolidays;
                double monthHours = finalWorkingDays * hours;
                double rate = m.getBillRate() != null ? m.getBillRate() : 0;
                double monthRevenue = rate * monthHours;

                YearMonth current = YearMonth.of(year, month);
                YearMonth previous = current.minusMonths(1);
                int prevYear = previous.getYear();
                int prevMonth = previous.getMonthValue();
                int prevWorkingDays = getWorkingDaysExcludingWeekends(prevYear, prevMonth);
                int prevAssociateHolidays = getAssociateHolidayDays(m.getAssociateId(), prevMonth);
                int prevCognizantHolidays = getCognizantHolidayDays(m.getCity(), prevMonth);
                int finalPrevWorkingDays = prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays;
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

        int start = page * size;
        int end = Math.min(start + size, filteredList.size());
        List<MostLikelyResponseDTO> pagedList = filteredList.subList(start, end);

        long endTime = System.currentTimeMillis();
        logger.info("MostLikely calculation completed in {} ms", endTime - startTime);

        return new PageImpl<>(pagedList, PageRequest.of(page, size), filteredList.size());
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
