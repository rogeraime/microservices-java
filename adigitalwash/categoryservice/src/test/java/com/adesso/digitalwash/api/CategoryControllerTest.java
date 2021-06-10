package com.adesso.digitalwash.api;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.services.CategoryService;
import com.adesso.digitalwash.services.LaundryService;

public class CategoryControllerTest {
	private CategoryService categoryServiceMock;
	private LaundryService laundryServiceMock;
	private CategoryController testSubject;
	
	
	@Before
	public void init() {
		categoryServiceMock = Mockito.mock(CategoryService.class);
		laundryServiceMock = Mockito.mock(LaundryService.class);
        testSubject = new CategoryController(categoryServiceMock, laundryServiceMock);
	}
	
	@Test
	public void getAllPricesReturnsAllGivenPrices() {
		//Given
		Category categoryOne = new Category("T-Shirt", new BigDecimal(35.00), (long)1.00);
		Category categoryTwo = new Category("Anzug", new BigDecimal(5.00), (long)1.00);
		Category categoryThree = new Category("Bettwaesche", new BigDecimal(3.00), (long)1.00);
		
		Collection<Category> allCategories = new ArrayList<>();
		allCategories.add(categoryOne);
		allCategories.add(categoryTwo);
		allCategories.add(categoryThree);
		
		Collection<BigDecimal> res = new ArrayList<>();
		res.add(categoryOne.getPrice());
		res.add(categoryTwo.getPrice());
		res.add(categoryThree.getPrice());
		
		//When
		Mockito.when(categoryServiceMock.findAllCategories()).thenReturn(allCategories);
		
		//Then
		assertThat(testSubject.getAllPrices()).isEqualTo(res);
	}
	
	@Test
	public void getActiveCategoriesReturnsAllActiveGivenCategories() {
		//Given
		Category categoryOne = new Category("T-Shirt", new BigDecimal(35.00), (long)1.00);
		Category categoryTwo = new Category("Anzug", new BigDecimal(5.00), (long)1.00);
		Category categoryThree = new Category("Bettwaesche", new BigDecimal(3.00), (long)1.00);
		categoryTwo.setActive(false);
		categoryTwo.setActiveCache(false);
		
		Collection<Category> allCategories = new ArrayList<>();
		allCategories.add(categoryOne);
		allCategories.add(categoryTwo);
		allCategories.add(categoryThree);
		
		Collection<Category> filteredCategories = new ArrayList<>();
		filteredCategories.add(categoryOne);
		filteredCategories.add(categoryThree);
		
		Mockito.when(categoryServiceMock.findAllCategories()).thenReturn(allCategories);
		
		//When
		assertThat(testSubject.getActiveCategories())
		
		//Then
		.isEqualTo(filteredCategories);
	}
	
	@Test
	public void updateCategoriesWithCategoryArrayReturnsUpdatedCategories() {
		//Given
		Category categoryOne = new Category("T-Shirt", new BigDecimal(35.00), (long)1.00);
		Category categoryTwo = new Category("Anzug", new BigDecimal(5.00), (long)1.00);
		Category categoryThree = new Category("Bettwaesche", new BigDecimal(3.00), (long)1.00);
		
		Category updatedCategories[] = new Category[3];
		updatedCategories[0] = categoryOne;
		updatedCategories[1] = categoryTwo;
		updatedCategories[2] = categoryThree;
		
		Mockito.when(categoryServiceMock.createOrUpdateCategory(categoryThree)).thenReturn(categoryThree);
		
		//When
		assertThat(testSubject.updateCategories(updatedCategories))
		
		//Then
		.isEqualTo(updatedCategories);
	}
	
	@Test
	public void updatePricesWithNewPrice4() {
		//Given
		Category categoryOne = new Category("T-Shirt", new BigDecimal(35.00), (long)1.00);
		Category categoryTwo = new Category("Anzug", new BigDecimal(5.00), (long)1.00);
		Category categoryThree = new Category("Bettwaesche", new BigDecimal(3.00), (long)1.00);
		categoryThree.setPriceCache(new BigDecimal(4.00));
		
		Collection<Category> allCategories = new ArrayList<>();
		allCategories.add(categoryOne);
		allCategories.add(categoryTwo);
		allCategories.add(categoryThree);
		
		Mockito.when(categoryServiceMock.findAllCategories()).thenReturn(allCategories);
		
		//When
		testSubject.updatePrices();
		
		//Then
		assertThat(categoryThree.getPrice()).isEqualTo(new BigDecimal(4.00));
	}
	
	@Test
	public void activateOrDeactivateCategory() {
		//Given
		Category categoryOne = new Category("T-Shirt", new BigDecimal(35.00), (long)1.00);
		Category categoryTwo = new Category("Anzug", new BigDecimal(5.00), (long)1.00);
		Category categoryThree = new Category("Bettwaesche", new BigDecimal(3.00), (long)1.00);
		categoryThree.setActive(true);
		categoryThree.setActiveCache(false);
		
		Collection<Category> allCategories = new ArrayList<>();
		allCategories.add(categoryOne);
		allCategories.add(categoryTwo);
		allCategories.add(categoryThree);
		
		Mockito.when(categoryServiceMock.findAllCategories()).thenReturn(allCategories);
		
		//When
		testSubject.activateOrDeactivateCategory();
		
		//Then
		assertThat(categoryThree.isActive()).isEqualTo(false);
	}
}
