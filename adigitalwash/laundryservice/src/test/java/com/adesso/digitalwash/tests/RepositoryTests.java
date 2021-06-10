package com.adesso.digitalwash.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

//import com.adesso.digitalwash.repositories.CategoryRepository;
import com.adesso.digitalwash.repositories.ClothRepository;
import com.adesso.digitalwash.repositories.LaundryRepository;

@DataJpaTest
@RunWith(SpringRunner.class)
public class RepositoryTests {

	@Autowired
	private ClothRepository clothRepository;
	@Autowired
	private LaundryRepository laundryRepository;
//	@Autowired
//	private CategoryRepository categoryRepository;

	//
	@Test
	public void laundryRepositoryLoaded() throws Exception {
		assertNotNull(laundryRepository);
	}

//	@Test
//	public void categoryRepositoryLoaded() throws Exception {
//		assertNotNull(categoryRepository);
//	}

	@Test
	public void clothRepositoryLoaded() throws Exception {
		assertNotNull(clothRepository);
	}

//	@Test
//	public void insertCategoriesT() throws Exception {
//		long cats = categoryRepository.count();
//		long clothes = clothRepository.count();
//		long laundry = laundryRepository.count();
//		//assertTrue(cats == 2 && clothes == 2 && laundry == 1);
//	}
}
