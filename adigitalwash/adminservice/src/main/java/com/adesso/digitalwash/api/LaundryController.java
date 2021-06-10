package com.adesso.digitalwash.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.services.LaundryService;

@RestController
@RequestMapping("/adminservice")
public class LaundryController {
	
	private LaundryService laundryService;

	@Autowired
	public LaundryController(LaundryService laundryService) {
		this.laundryService = laundryService;
	}
	
	@GetMapping("/laundries/{id}")
	public Laundry getLaundry(@PathVariable(value = "id") long id) {
		return laundryService.getLaundry(id);
	}

	@GetMapping("/get-laundries/{date}")
	public List<Laundry> getLaundriesByDate(
			@PathVariable(value = "date") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate date) {
		return laundryService.getLaundriesByAcceptedDateBetweenFirstDayAfterPreviousAcceptedDateUntilCurrentOne(date);
	}

	@PutMapping("/laundries/")
	public ResponseEntity<Collection<Laundry>> getLaundries(@RequestBody Collection<Long> id) {
		if (id.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(laundryService.getLaundriesByIds(id), HttpStatus.OK);
	}

	@GetMapping("/laundries/{id}/paid")
	public Laundry setLaundryPaid(@PathVariable(value = "id") Long id) {
		Laundry laundry = laundryService.getLaundry(id);
		if (laundry.getPaid().equals(laundry.getTotalCost())) laundry.setPaid(new BigDecimal(0));
		else laundry.setPaid(laundry.getTotalCost());
		return laundryService.updateLaundry(laundry);
	}

}
