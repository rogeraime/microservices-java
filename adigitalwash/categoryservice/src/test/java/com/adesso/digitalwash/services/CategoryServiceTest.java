package com.adesso.digitalwash.services;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.repositories.CategoryRepository;

public class CategoryServiceTest {
	private CategoryRepository categoryRepositoryMock;
	private CategoryService testSubject;
	
	
	@Before
	public void init() {
		categoryRepositoryMock = Mockito.mock(CategoryRepository.class);
        testSubject = new CategoryService(categoryRepositoryMock);
	}
	
	@Test
	public void findAllCategories() {
		//Given
		Category categoryOne = new Category("T-Shirt", new BigDecimal(35.00), (long)1.00);
		Category categoryTwo = new Category("Anzug", new BigDecimal(5.00), (long)1.00);
		Category categoryThree = new Category("Bettwaesche", new BigDecimal(3.00), (long)1.00);
		
		Collection<Category> categories = new ArrayList<>();
		categories.add(categoryOne);
		categories.add(categoryTwo);
		categories.add(categoryThree);
		
		Iterable<Category> allCategories = new ArrayList<>(categories);
		Mockito.when(categoryRepositoryMock.findAll()).thenReturn(allCategories);
		
		//When
		assertThat(testSubject.findAllCategories())
		
		//Then
		.isEqualTo(categories);
	}
}
