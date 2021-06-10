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

	public CategoryService() {
		rest = new AsyncRestTemplate();
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
	
	public Collection<Category> getActiveCategories(HttpEntity<HttpHeaders> entity) {
		String uri = "/categoryservice/categories/active";
		try {
			return rest.exchange(CategoryServer + uri, HttpMethod.GET, entity, Collection.class).get().getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}