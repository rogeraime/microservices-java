package com.adesso.digitalwash.api;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.model.Transporter;
import com.adesso.digitalwash.services.PdfService;

@Controller
@RequestMapping("/pdfservice")
public class PdfController {

	public PdfService pdfService;
	
	@Autowired
	public PdfController(PdfService pdfService) {
		this.pdfService = pdfService;
	}
	
	@PostMapping(value = "/mailpdf")
	@ResponseBody
	public byte[] getMailPdf(@RequestBody Transporter<Laundry, ArrayList<Category>> transporter) {
		Laundry laundry =  transporter.getT();
		Collection<Category> categories = transporter.getK();
		return pdfService.createMailPdf(laundry, categories);
	}
	
	@PostMapping(value = "/exportshirtspdf")
	@ResponseBody
	public byte[] getExportShirtsPdf(@RequestBody Transporter<ArrayList<Laundry>, ArrayList<Category>> transporter) {
		ArrayList<Laundry> laundries =  transporter.getT();
		Collection<Category> categories = transporter.getK();
		return pdfService.createExportShirtsPdf(laundries, categories);
	}
	
	@PostMapping(value = "/exportnonshirtspdf")
	@ResponseBody
	public byte[] getExportNonShirtsPdf(@RequestBody Transporter<ArrayList<Laundry>, ArrayList<Category>> transporter) {
		ArrayList<Laundry> laundries =  transporter.getT();
		Collection<Category> categories = transporter.getK();
		return pdfService.createExportNonShirtsPdf(laundries, categories);
	}
}
