package com.adesso.digitalwash.api;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.services.LaundryService;

public class LaundryControllerUnitTest {
	
	private LaundryService laundryServiceMock;
	private LaundryController testSubject;
	
	@Before
	public void init() {
		laundryServiceMock = Mockito.mock(LaundryService.class);
		testSubject = new LaundryController(laundryServiceMock);
	}
	
	@Test
	public void setLaundryPaidForLaundryWithPaidUnequalTotalCostReturnsLaundryWithPaidEqualsTotalCost() {
		//Given
		Laundry laundry = new Laundry(1);
		laundry.setPaid(BigDecimal.ZERO);
		laundry.setTotalCost(new BigDecimal(10));
		Mockito.when(laundryServiceMock.getLaundry((long) 1)).thenReturn(laundry);
		Mockito.when(laundryServiceMock.updateLaundry(laundry)).thenReturn(laundry);
		//When
		assertThat(testSubject.setLaundryPaid((long) 1).getPaid())
		//Then
		.isEqualTo(laundry.getTotalCost());
	}
	
	@Test
	public void setLaundryPaidForLaundryWithPaidEqualTotalCostReturnsLaundryWithPaidEquals0() {
		//Given
		Laundry laundry = new Laundry(1);
		laundry.setPaid(new BigDecimal(10));
		laundry.setTotalCost(new BigDecimal(10));
		Mockito.when(laundryServiceMock.getLaundry((long) 1)).thenReturn(laundry);
		Mockito.when(laundryServiceMock.updateLaundry(laundry)).thenReturn(laundry);
		//When
		assertThat(testSubject.setLaundryPaid((long) 1).getPaid())
		//Then
		.isEqualTo(BigDecimal.ZERO);
	}
	
}
