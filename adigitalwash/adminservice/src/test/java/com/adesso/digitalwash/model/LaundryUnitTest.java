package com.adesso.digitalwash.model;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LaundryUnitTest {
	
	@Test
	public void getClothCountOfCategoryreturns7ForCategory1() {
		//Given
		int testCount = 7;
		long testId = 1;
		List<Cloth> clothes = new ArrayList<Cloth>();
		clothes.add(new Cloth(testCount, new BigDecimal(12), testId));
		clothes.add(new Cloth(5, new BigDecimal(12), (long) 2));
		clothes.add(new Cloth(2, new BigDecimal(12), (long) 3));
		clothes.add(new Cloth(6, new BigDecimal(12), (long) 4));
		clothes.add(new Cloth(3, new BigDecimal(12), (long) 5));
		Laundry testSubject = new Laundry(clothes);
		//When
		assertThat(testSubject.getClothCountOfCategory(testId))
		//Then
		.isEqualTo(testCount);
	}
	
	@Test
	public void getClothCountOfCategoryreturns0ForCategory1() {
		
	}
	
	@Test
	public void getClothCountOfCategoryreturns6ForCategory3() {
		
	}
	
	
}
