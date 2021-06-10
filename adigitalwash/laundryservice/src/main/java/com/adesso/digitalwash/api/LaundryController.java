package com.adesso.digitalwash.api;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Cloth;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.model.Mail;
import com.adesso.digitalwash.services.AuthService;
import com.adesso.digitalwash.services.CategoryService;
import com.adesso.digitalwash.services.LaundryService;
import com.adesso.digitalwash.services.NotificationService;
import com.adesso.digitalwash.services.PdfService;

@RestController
@RequestMapping("/laundryservice")
public class LaundryController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(LaundryController.class);


	@Autowired
	private LaundryService laundryService;

	@Autowired
	public NotificationService emailService;

	@Autowired
	public PdfService pdfService;

	@Autowired
	public CategoryService categoryService;

	@Autowired
	public AuthService authService;
	
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	public LaundryController() {
		initSendReminderScheduling();
	}

	public Collection<Category> getCategories(HttpHeaders headers) {
		return categoryService.getAllCategories(new HttpEntity<>(headers));
	}

	public Category getCategoryById(Long id, HttpHeaders headers) {
		return categoryService.getCategoryById(id, new HttpEntity<>(headers));
	}

	public Collection<Category> getActiveCategories(HttpHeaders headers) {
		return categoryService.getActiveCategories(new HttpEntity<>(headers));
	}

	@GetMapping("/laundries")
	public Collection<Laundry> getLaundriesByLaundryOwner(@RequestHeader("Authorization") String authToken)
			throws Exception {
		String claims = JwtHelper.decode(authToken.substring(7)).getClaims();
		String name = new JSONObject(claims).getString("user_full_name");
		return laundryService.findLaundriesByLaundryOwner(name);
	}

	@PutMapping("/laundries/{id}/completed")
	public ResponseEntity<HttpStatus> setCompleted(@PathVariable(value = "id") Long id,
			@RequestHeader("Authorization") String authToken) {
		Laundry laundry = laundryService.getLaundryById(id);

		String claims = JwtHelper.decode(authToken.substring(7)).getClaims();
		try {
			String name = new JSONObject(claims).getString("user_full_name");
			
			LocalDate deliveryDate = laundry.getDeliveryDate();
			LocalDate currentDate = LocalDate.now();
			
			if ((deliveryDate.isBefore(currentDate) || deliveryDate.isEqual(currentDate))
					&& laundry.getLaundryOwner().equals(name)) {
				laundry.setCompleted(!laundry.isCompleted());
				laundryService.updateLaundry(laundry);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Laundry could not be completed.");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/create-laundry/{sendMail}")
	public ResponseEntity<Laundry> laundryCreate(@PathVariable boolean sendMail, @RequestBody Laundry laundryToCreate,
			@RequestHeader("Authorization") String authToken) throws Exception {
		JSONObject claims = new JSONObject(JwtHelper.decode(authToken.substring(7)).getClaims());
		
		laundryToCreate.setLaundryOwner(claims.getString("user_full_name"));
		laundryToCreate.setEMail(claims.getString("user_eMail"));
		laundryToCreate.setSubmissionDate(LocalDate.now());
		laundryToCreate.setAcceptedDate(getAcceptedDate());
		laundryToCreate.setDeliveryDate(getDeliveryDate());
		laundryToCreate.assignClothesToLaundry(laundryToCreate.getClothes());
		laundryToCreate.setPaid(new BigDecimal(0));
		laundryToCreate.computeTotalCost();
		
		if (sendMail) {
			sendCreatedMail(laundryToCreate, getHeaders(authToken));
		}
		
		laundryService.addLaundry(laundryToCreate);
		
		return new ResponseEntity<Laundry>(laundryToCreate, HttpStatus.OK);
	}
	
	/**
	 * Calculates the next delivery date in relation to the next accepted date.
	 * 
	 * Example: 
	 * Monday and Thursday are collection/delivery days.
	 * On Monday, Tuesday and Wednesday this method will return the date of the following week´s Monday,
	 * because the accepted date (collection of the laundries will be Thursday --> delivery day: Monday).
	 * 
	 * @return delivery date in relation to the next accepted date
	 * @see LaundryController#getAcceptedDate()
	 */
	private LocalDate getDeliveryDate() {
		List<Integer> acceptedDaysOfWeek = new ArrayList<Integer>(getWeekdays());
		Collections.sort(acceptedDaysOfWeek);
		int daysOfWeekSize = acceptedDaysOfWeek.size();
		
		LocalDate acceptedDate = getAcceptedDate();
		
		if (daysOfWeekSize == 1 ) {
			return acceptedDate.plusDays(7);
		} else if (daysOfWeekSize > 1) {
			Integer weekDayOfDeliverDay;
			
			int weekDayOfAcceptedDate = acceptedDate.getDayOfWeek().getValue();
			int indexOfAcceptedDate = acceptedDaysOfWeek.indexOf(weekDayOfAcceptedDate);
			
			if (indexOfAcceptedDate == acceptedDaysOfWeek.size() - 1) { // indexOfAcceptedDate is the last entry of acceptedDaysOfWeek
				weekDayOfDeliverDay = acceptedDaysOfWeek.get(0);
				
				return acceptedDate.plusDays(7 - weekDayOfAcceptedDate + weekDayOfDeliverDay);
			} else {
				weekDayOfDeliverDay = acceptedDaysOfWeek.get(indexOfAcceptedDate + 1);
				
				return acceptedDate.plusDays(weekDayOfDeliverDay - weekDayOfAcceptedDate);
			}
		} else {
			LOGGER.error("Unexpected case: collection given from getWeekdays() is empty! Therefore NULL returned for getDeliveryDate() !!!");
		}

		return null;
	}

	/**
	 * Calculates the next accepted date in relation to today and the weekdays when laundry is accepted(collected)/delivered.
	 * Therefore the earliest accepted day will always be tomorrow.
	 * 
	 * Example: 
	 * Monday and Thursday are collection(accepted)/delivery days.
	 * On Monday, Tuesday and Wednesday this method will return the date of Thursday.
	 * 
	 * @return next accepted date which is dependent on the weekdays when laundry is collected/delivered
	 */
	private LocalDate getAcceptedDate() {
		LocalDate now = LocalDate.now();
		LocalDate localDateToReturn = now;
		int todaysWeekDay = now.getDayOfWeek().getValue();
		
		List<Integer> acceptedDaysOfWeek = new ArrayList<Integer>(getWeekdays());
		Collections.sort(acceptedDaysOfWeek);
		int daysOfWeekSize = acceptedDaysOfWeek.size();

		if (daysOfWeekSize == 1) {
			Integer acceptedDayWeekDay = acceptedDaysOfWeek.get(0);
			localDateToReturn = getFirstOccurenceForWeekDay(acceptedDayWeekDay);
		} else if (daysOfWeekSize > 1) {
			int indexOfNextAcceptedWeekDay = getIndexOfNextAcceptedWeekDayFromListForCurrentWeekDay(acceptedDaysOfWeek, todaysWeekDay);
								 
			int weekDayOfNextAcceptedDate = acceptedDaysOfWeek.get(indexOfNextAcceptedWeekDay);
			
			localDateToReturn = getFirstOccurenceForWeekDay(weekDayOfNextAcceptedDate);
			
		} else {
			LOGGER.error("Unexpected case: collection given from getWeekdays() is empty! Therefore 'now' is returned as nextAcceptedDate!");
		}
		
		return localDateToReturn;
	}
	
	/**
	 * Returns the index of the next accepted week day from weekDaysList in relation to currentWeekDay.
	 * 
	 * For example:
	 * Monday (index 0) and Thursday (index 1) are collection(accepted)/delivery days.
	 * 
	 * On Monday, Tuesday and Wednesday this method will return the index of Thursday which is 1.
	 * 
	 * @param weekDaysList holds the accepted/deliver days
	 * @param currentWeekDay marks the starting point for getting next accepted week day
	 * @return index from weekDaysList for the next accepted week day (in relation to currentWeekDay)
	 */
	private int getIndexOfNextAcceptedWeekDayFromListForCurrentWeekDay(List<Integer> weekDaysList, int currentWeekDay) {
		int currentWeekDayToSearchForInAcceptedWeekDays = currentWeekDay;
		
		// add one day because the earliest next accepted date could be tomorrow!
		currentWeekDayToSearchForInAcceptedWeekDays = addOneDay(currentWeekDayToSearchForInAcceptedWeekDays);
		
		int indexToReturn = 0;
		
		for (int i = 1; i <= 7; i++) {
			indexToReturn = weekDaysList.indexOf(currentWeekDayToSearchForInAcceptedWeekDays);
			if (indexToReturn != -1) {
				break;
			} else {
				currentWeekDayToSearchForInAcceptedWeekDays = addOneDay(currentWeekDayToSearchForInAcceptedWeekDays);
			}
		}
		return indexToReturn;
	}
	
	/**
	 * Adds one day to originWeekDay. If originWeekDay is 7 the returned value will be 1, because there are only 7 days in a week (next day after 7 is 1). 
	 * 
	 * @param originWeekDay
	 * @return originWeekDay + 1
	 */
	private int addOneDay(int originWeekDay) {
		if (originWeekDay == 7) {
			originWeekDay = 1; // reset to 1 means here: one day added to 7 (last day in week) 
		} else {				
			originWeekDay++; // ++: one day added
		}
		return originWeekDay;
	}
	
	private LocalDate getFirstOccurenceForWeekDay(Integer weekDay) {
		TemporalAdjuster firstOccurence = TemporalAdjusters.next(DayOfWeek.of(weekDay));
		LocalDate firstOccurenceAsDate = LocalDate.now(ZoneId.of("UTC")).with(firstOccurence);
		
		return firstOccurenceAsDate;
	}


	@PutMapping("/update-laundry/{sendMail}")
	public ResponseEntity<Laundry> LaundryCreatePut(@PathVariable boolean sendMail, @RequestBody Laundry updatedLaundry,
			@RequestHeader("Authorization") String authToken) throws Exception {
		String claims = JwtHelper.decode(authToken.substring(7)).getClaims();
		String name = new JSONObject(claims).getString("user_full_name");
		// Get Laundry from DB to ensure that users only can update number of clothes &
		// image
		Laundry laundry = laundryService.getLaundryById(updatedLaundry.getId());
		if (laundry.getAcceptedDate().isAfter(LocalDate.now()) && laundry.getLaundryOwner().equals(name)) {
			laundry.setClothes(updatedLaundry.getClothes());
			laundry.setImage(updatedLaundry.getImage());
			laundry.setTotalCost(new BigDecimal(0));
			laundry.computeTotalCost();
			laundry.getClothes().forEach(c -> c.setLaundry(laundry));
			laundryService.updateLaundry(laundry);
			if (sendMail) {
				sendCreatedMail(laundry, getHeaders(authToken));
			}
			return new ResponseEntity<Laundry>(laundry, HttpStatus.OK);
		}
		return new ResponseEntity<Laundry>(laundry, HttpStatus.CONFLICT);
	}

	@DeleteMapping("/delete-laundry/{id}")
	public ResponseEntity<HttpStatus> LaundryDelete(@PathVariable Long id,
			@RequestHeader("Authorization") String authToken) {
		String claims = JwtHelper.decode(authToken.substring(7)).getClaims();
		try {
			String name = new JSONObject(claims).getString("user_full_name");
			Laundry laundry = laundryService.getLaundryById(id);
			if (laundry.getAcceptedDate().isAfter(LocalDate.now()) && laundry.getLaundryOwner().equals(name)) {
				laundryService.deleteLaundryWithId(id);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Laundry could not be deleted.");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/weekdays")
	public Collection<Integer> getWeekdays() {
		return java.util.Arrays.asList(1, 4);
	}

	@Async
	private void sendCreatedMail(Laundry laundry, HttpHeaders headers) {
		Mail mail = new Mail();
		mail.setTo(laundry.geteMail());
		mail.setSubject("Digitaler Hemdenservice, Rechnung für " + laundry.getLaundryOwner());
		if (laundry.getTotalCost().floatValue() > 0) {
			mail.setAttachment(pdfService.getMailPdf(laundry, (ArrayList<Category>) getCategories(headers),
					new HttpEntity<>(headers)));
		}
		mail.setText(getMailText(laundry, (mail.getAttachment() != null), headers));
		emailService.sendMail(mail, headers);
	}

	@Async
	private void sendReminderMail(Laundry laundry, HttpHeaders headers) {
		Mail mail = new Mail();
		mail.setTo(laundry.geteMail());
		mail.setSubject("Digitaler Hemdenservice, Erinnerung für " + laundry.getLaundryOwner());
		mail.setText(getReminderText(laundry, headers));
		emailService.sendMail(mail, headers);
	}

	private String getMailText(Laundry laundry, boolean attachement, HttpHeaders headers) {
		String mailtext = "Hallo " + laundry.getLaundryOwner() + "," + "\n" + "\n"
				+ "Du hast einen neuen Wäscheeintrag bei DigiWash angelegt. Hier sind deine Wäschedaten:" + "\n" + "\n";

		mailtext += getNameAndClothCountFor(laundry, headers) + "\n";

		if (attachement) {
			mailtext += "\n" + "Im Anhang befindet sich deine Rechnung.";
		}

		if (laundry.getTotalCost().floatValue() > 0) {
			mailtext += getPayLaundryMessage() + "\n";
		}

		mailtext += getEmailFooter();
		return mailtext;
	}

	private String getReminderText(Laundry laundry, HttpHeaders headers) {
		String mailtext = "Hallo " + laundry.getLaundryOwner() + "," + "\n" + "\n" + "Du hast deine Wäsche vom "
				+ laundry.getAcceptedDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")) + " noch nicht bezahlt:"
				+ "\n" + "\n";

		mailtext += getNameAndClothCountFor(laundry, headers);

		mailtext += getPayLaundryMessage() + getEmailFooter();
		return mailtext;
	}

	private String getPayLaundryMessage() {
		return "\nBitte begleiche die Gesamtkosten, indem du das Geld in einem beschriftetem Umschlag am Empfang abgibst.";
	}

	private String getEmailFooter() {
		return "\n" + "Viele Grüße," + "\n" + "DigiWash";
	}

	private String getNameAndClothCountFor(Laundry laundry, HttpHeaders headers) {
		Collection<Category> categories = getActiveCategories(headers);
		String mailtext = "";
		for (int i = 0; i < categories.size(); i++) {
			Cloth cloth = laundry.getClothes().get(i);
			if (cloth.getClothCount() != 0) {
				mailtext += String.format("%-15s \t %15s \n", getCategoryById(cloth.getCategoryId(), headers).getName(),
						cloth.getClothCount());
			}
		}
		mailtext += "Gesamtkosten: " + String.format(Locale.GERMAN, "%.2f", laundry.getTotalCost().floatValue()) + "€";
		return mailtext;
	}

	private void initSendReminderScheduling() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = getThreadPoolTaskScheduler();
        threadPoolTaskScheduler.schedule(sendReminder(), new CronTrigger(getCronExpression()));
	}
	
	private Runnable sendReminder() {
		return () -> {
			Collection<Laundry> laundries = laundryService.findAllLaundries();
			for (Laundry laundry : laundries) {
				if (laundry.getPaid().floatValue() < laundry.getTotalCost().floatValue()) {
					String authToken = authService.getToken();
					sendReminderMail(laundry, getHeaders("bearer " + authToken));
				}
			}
		};
	}
	
	private String getCronExpression() {
		String cronStringWithoutWeekDays = "0 0 6 ? * ";
		String weekDaysToAppendForCron = StringUtils.join(getWeekdays(), ",");
		
		String cronComplete = cronStringWithoutWeekDays + weekDaysToAppendForCron;
		
		return cronComplete;
	}

	private HttpHeaders getHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", authToken);
		return headers;
	}
	
	private ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
		if (threadPoolTaskScheduler != null) {
			return threadPoolTaskScheduler;
		} else {
			threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
			threadPoolTaskScheduler.setPoolSize(1);
			threadPoolTaskScheduler.initialize();
		}
		return threadPoolTaskScheduler;
	}
}
