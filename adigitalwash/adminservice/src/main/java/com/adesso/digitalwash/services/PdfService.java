package com.adesso.digitalwash.services;

import java.util.ArrayList;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.model.Transporter;

@Service
public class PdfService {
	private String PdfServer = "http://localhost:8080";
	private RestTemplate rest;

	public PdfService() {
		this.rest = new RestTemplate();
	}

	public byte[] getExportShirtsPdf(ArrayList<Laundry> laundries, ArrayList<Category> categories, HttpEntity<HttpHeaders> entity) {
		String uri = "/pdfservice/exportshirtspdf";
		Transporter<ArrayList<Laundry>, ArrayList<Category>> transporter = new Transporter<>(laundries, categories);
		try{
			return rest.exchange(PdfServer + uri, HttpMethod.POST, new HttpEntity<>(transporter, entity.getHeaders()), byte[].class).getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] getExportNonShirtsPdf(ArrayList<Laundry> laundries, ArrayList<Category> categories, HttpEntity<HttpHeaders> entity) {
		String uri = "/pdfservice/exportnonshirtspdf";
		Transporter<ArrayList<Laundry>, ArrayList<Category>> transporter = new Transporter<>(laundries, categories);
		try{
			return rest.exchange(PdfServer + uri, HttpMethod.POST, new HttpEntity<>(transporter, entity.getHeaders()), byte[].class).getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
