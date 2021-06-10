package com.adesso.digitalwash.services;

import java.util.Collection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import com.adesso.digitalwash.model.Category;

@Service
public class CategoryService {
	private String CategoryServer = "http://localhost:8080";
	private AsyncRestTemplate rest;
	private HttpHeaders headers;

	public CategoryService() {
		this.rest = new AsyncRestTemplate();
		this.headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");
	}

	public Collection<Category> getAllCategories(HttpEntity<HttpHeaders> entity) {
		String uri = "/categoryservice/categories";
		try {
			return rest.exchange(CategoryServer + uri, HttpMethod.GET, entity, Collection.class).get().getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Category getCategoryById(Long id, HttpEntity<HttpHeaders> entity) {
		String uri = "/categoryservice/" + id;
		try {
			return rest.exchange(CategoryServer + uri, HttpMethod.GET, entity, Category.class).get().getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}