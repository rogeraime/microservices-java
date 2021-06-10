package com.adesso.digitalwash.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.services.CategoryService;
import com.adesso.digitalwash.services.LaundryService;
import com.adesso.digitalwash.services.PdfService;

@RestController
@RequestMapping("/export")
public class ExportController {

	@Autowired
	LaundryService laundryService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	PdfService pdfService;

	@GetMapping(value = "/shirts{date}")
	public ResponseEntity<byte[]> exportShirts(
			@PathVariable(value = "date") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate date,
			@CookieValue("authToken") String token) {
		ResponseEntity<byte[]> result = null;
		try {
			String authToken = "bearer " + token;
			Category shirtCategory = categoryService.getCategoryById((long) 1, new HttpEntity<>(getHeaders(authToken)));
			List<Laundry> laundries = laundryService.getLaundriesByAcceptedDateBetweenFirstDayAfterPreviousAcceptedDateUntilCurrentOne(date).stream().filter(l -> {
				return l.getClothes().stream().anyMatch(c -> c.getCategoryId().equals(shirtCategory.getId()));
			}).collect(Collectors.toList());
			ArrayList<Category> categories = (ArrayList<Category>) categoryService
					.getAllCategories(new HttpEntity<>(getHeaders(authToken)));
			byte pdfbytes[] = pdfService.getExportShirtsPdf((ArrayList<Laundry>) laundries, categories,
					new HttpEntity<>(getHeaders(authToken)));
			ByteArrayOutputStream baos = new ByteArrayOutputStream(pdfbytes.length);
			baos.write(pdfbytes, 0, pdfbytes.length);
			result = prepareDownload(baos, "shirts.pdf");
		} catch (IOException ex) {
			result = new ResponseEntity<byte[]>(HttpStatus.CONFLICT);
		}
		return result;
	}

	@GetMapping("/nonshirts{date}")
	public ResponseEntity<byte[]> exportNonShirts(
			@PathVariable(value = "date") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate date,
			@CookieValue("authToken") String token) {
		ResponseEntity<byte[]> result = null;
		try {
			String authToken = "bearer " + token;
			byte pdfbytes[] = pdfService.getExportNonShirtsPdf(
					(ArrayList<Laundry>) laundryService.getLaundriesByAcceptedDateBetweenFirstDayAfterPreviousAcceptedDateUntilCurrentOne(date),
					(ArrayList<Category>) categoryService.getAllCategories(new HttpEntity<>(getHeaders(authToken))),
					new HttpEntity<>(getHeaders(authToken)));
			ByteArrayOutputStream baos = new ByteArrayOutputStream(pdfbytes.length);
			baos.write(pdfbytes, 0, pdfbytes.length);
			result = prepareDownload(baos, "nonshirts.pdf");
		} catch (IOException ex) {
			result = new ResponseEntity<byte[]>(HttpStatus.CONFLICT);
		}
		return result;
	}

	// prepare pdf files for download by attaching the corresponding headers to the
	// response
	private ResponseEntity<byte[]> prepareDownload(ByteArrayOutputStream os, String filename) throws IOException {
		byte[] contents = os.toByteArray();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	}

	private HttpHeaders getHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", authToken);
		return headers;
	}
}
