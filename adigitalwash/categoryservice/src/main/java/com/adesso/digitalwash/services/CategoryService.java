package com.adesso.digitalwash.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.repositories.CategoryRepository;

@Service
public class CategoryService {

	private CategoryRepository categoryRepository;
	
	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public Category createOrUpdateCategory(Category category) {
		return categoryRepository.save(category);
	}

	public Category findCategoryById (Long id) {
		return categoryRepository.findById(id).get();
	}

	public void deleteCategoryWithId(Long id) {
		categoryRepository.deleteById(id);
	}

	public Collection<Category> findAllCategories() {
		Collection<Category> categories = new ArrayList<Category>();
		categoryRepository.findAll().forEach(c -> categories.add(c));
		return categories;
	}
	
//	public <T> T readValues(String stream, Class<T> obj) {
//		ObjectMapper m = new ObjectMapper();
//		try {
//			return m.readValue(stream, obj);
//		} catch (IOException e) {
//			throw new UncheckedIOException(e);
//		}
//	}
}
