package com.adesso.digitalwash.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.repositories.LaundryRepository;

@Service
public class LaundryService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(LaundryService.class);

	@Autowired
	private LaundryRepository laundryRepository;
	
	@Value("${protocol.http}")
	private String http;
	
	@Value("${server.ip}")
	private String serverIp;
	
	@Value("${server.allowedOrigin}")
	private String port;
	
	@Value("${services.laundryservice}")
	private String service;
	
	@Value("${rest.weekdays}")
	private String restMappingName;

	public List<Laundry> getLaundriesByAcceptedDateBetweenFirstDayAfterPreviousAcceptedDateUntilCurrentOne(LocalDate queryCurrentAcceptedDate) {
		Integer dayOfWeekShownInCalendar = queryCurrentAcceptedDate.getDayOfWeek().getValue();
			
		int countOfDaysToSearchBeforeQueryDate = 0;
		
		int daysOfWeekSize = getWeekdays().size();
		
		if (daysOfWeekSize > 1) {
			Integer nearestPreviousAcceptedWeekDay = getNearestPreviousAcceptedWeekDay(dayOfWeekShownInCalendar);
			
			if (dayOfWeekShownInCalendar > nearestPreviousAcceptedWeekDay) {
				countOfDaysToSearchBeforeQueryDate = dayOfWeekShownInCalendar - nearestPreviousAcceptedWeekDay - 1; // -1 to get one day after nearestPreviousAcceptedWeekDay 
			} else if (dayOfWeekShownInCalendar < nearestPreviousAcceptedWeekDay) {
				countOfDaysToSearchBeforeQueryDate = (7 - nearestPreviousAcceptedWeekDay) + (dayOfWeekShownInCalendar - 1); // addition of 'dayOfWeekShownInCalendar - 1' to include count of days from weekDay 1 to dayOfWeekShownInCalendar
			}
		} else if (daysOfWeekSize == 1) {
			countOfDaysToSearchBeforeQueryDate = 6;
		} else {
			LOGGER.error("Unexpected case: collection given from getWeekdays() is empty!");
		}
		
		LocalDate startDate = queryCurrentAcceptedDate.minusDays(countOfDaysToSearchBeforeQueryDate);

		return laundryRepository.findByAcceptedDateBetween(startDate, queryCurrentAcceptedDate);
	}

	/**
	 * 
	 * @param dayOfWeekShownInCalendar
	 * @return nearest previous accepted weekday 
	 * 		   OR param value itself for the unexpected case that param value is not part of the collection given from getWeekdays()
	 * 		   OR param value itself for the unexpected case that the collection given from getWeekdays() is empty
	 */
	private Integer getNearestPreviousAcceptedWeekDay(Integer dayOfWeekShownInCalendar) {
		List<Integer> daysOfWeek = new ArrayList<Integer>(getWeekdays());
		Collections.sort(daysOfWeek);
		int daysOfWeekSize = daysOfWeek.size();
		
		Integer nearestPreviousAcceptedWeekDay = dayOfWeekShownInCalendar;
		
		if (daysOfWeekSize > 1) {
			int dayOfWeekIndex = daysOfWeek.indexOf(dayOfWeekShownInCalendar);

			if(dayOfWeekIndex != -1) {
				// dayOfWeekShownInCalendar is the first entry of daysOfWeek
				if (dayOfWeekIndex == 0) { 
					// nearestPreviousAcceptedWeekDay is the last entry of daysOfWeek
					nearestPreviousAcceptedWeekDay = daysOfWeek.get(daysOfWeekSize - 1);
				} else {
					// the entry of daysOfWeek which owns a one lower index than dayOfWeekShownInCalendar´s index is the nearestPreviousAcceptedWeekDay
					nearestPreviousAcceptedWeekDay = daysOfWeek.get(dayOfWeekIndex - 1);
				}
			} else {
				LOGGER.error("Unexpected case: " + dayOfWeekShownInCalendar + " is not part of daysOfWeek " + daysOfWeek + "!");
			}
		} else if (daysOfWeekSize == 1) {
			if (dayOfWeekShownInCalendar == 7) {
				nearestPreviousAcceptedWeekDay = 1;
			} else {
				nearestPreviousAcceptedWeekDay = dayOfWeekShownInCalendar + 1;
			}
		} else {
			LOGGER.error("Unexpected case: collection given from getWeekdays() is empty!");
		}

		return nearestPreviousAcceptedWeekDay;
	}

	public Laundry getLaundry(Long id) {
		return laundryRepository.findById(id).get();
	}

	public List<Laundry> getLaundriesByIds(Collection<Long> ids) {
		return laundryRepository.findByIdIn(ids);
	}

	public Laundry updateLaundry(Laundry laundry) {
		return laundryRepository.save(laundry);
	}
	public void deleteLaundry(Long id) {
		laundryRepository.deleteById(id);
	}
	
	public Collection<Integer> getWeekdays(){
		Collection<Integer> weekDays = null;
		
		RestTemplate restTemplate = new RestTemplate();
		weekDays = restTemplate.getForObject(getWeekDaysRestUrl(), Collection.class);

		return weekDays;
	}
	
	private String getWeekDaysRestUrl() {
		return http + serverIp + ":" + port + "/" + service + "/" + restMappingName;
	}
}
