package com.adesso.digitalwash.api;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.services.LaundryService;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LaundryControllerAPITest {
	
	@MockBean
	LaundryService laundryServiceMock;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void getLaundryWithId123returnsLaundryWithId123() throws Exception {
		//Arrange
		Gson g = new Gson();
		Laundry laundry = new Laundry(123);
		when(laundryServiceMock.getLaundry((long) 123)).thenReturn(laundry);
		//Act
		String content = this.mvc.perform(get("/adminservice/laundries/123"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
		//Assert
		Laundry expectedLaundry = g.fromJson(content, Laundry.class);
		assertThat(expectedLaundry).isEqualToComparingFieldByField(laundry);
	}
	
	@Test
	public void getLaundryWithId123returnsNull() throws Exception {
		//Arrange
		when(laundryServiceMock.getLaundry((long) 123)).thenReturn(null);
		//Act
		this.mvc.perform(get("/adminservice/laundries/123"))
		//Assert
		.andExpect(status().isOk()).equals(null);
	}
	
	@Test
	public void getLaundryByDateWithDate20190625andReturnLaundryList() throws Exception {
		//Arrange
		Gson g = new Gson();
		LocalDate date = LocalDate.of(2019,06,25);
		Laundry laundry = new Laundry(123);
		laundry.setAcceptedDate(date);
		List<Laundry> listLaundry = new ArrayList<Laundry>();
		listLaundry.add(laundry);
		when(laundryServiceMock.getLaundriesByAcceptedDateBetweenFirstDayAfterPreviousAcceptedDateUntilCurrentOne(date)).thenReturn(listLaundry);
		//Act
		String response = this.mvc.perform(get("/adminservice/get-laundries/2019-06-25"))
		.andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
		//Assert
		List responseList = g.fromJson(response, List.class);
		assertThat(responseList.get(0).toString()).contains("acceptedDate=2019-06-25");
	}
	
	@Test
	public void getLaundryByDateWithDate20190625andReturnNull() throws Exception {
		//Arrange
		LocalDate date = LocalDate.of(2019, 06, 25);
		when(laundryServiceMock.getLaundriesByAcceptedDateBetweenFirstDayAfterPreviousAcceptedDateUntilCurrentOne(date)).thenReturn(null);
		//Act
		this.mvc.perform(get("/adminservice/get-laundries/2019-06-25"))
		//Assert
		.andExpect(status().isOk()).equals(null);
	}
	
	@Test
    public void getLaundriesReturnsTwoLaundriesWithGivenIds() throws Exception{
          //Arrange
          Gson g = new Gson();
          ArrayList<Long> id = new ArrayList<Long>();
          id.add((long) 123);
          id.add((long) 345);
          List<Laundry> laundryList = new ArrayList<Laundry>();
          laundryList.add(new Laundry(id.get(0)));
          laundryList.add(new Laundry(id.get(1)));
          when(laundryServiceMock.getLaundriesByIds(id)).thenReturn(laundryList);
          //Act
          String response = this.mvc.perform(put("/adminservice/laundries/")
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .content(g.toJson(id)))
        	.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andReturn().getResponse().getContentAsString();
          Laundry responseCollection[] = g.fromJson(response, Laundry[].class);
          //Assert
          SoftAssertions softly = new SoftAssertions();
          softly.assertThat(responseCollection[0]).isEqualToComparingFieldByField(laundryList.get(0));
          softly.assertThat(responseCollection[1]).isEqualToComparingFieldByField(laundryList.get(1));
          softly.assertAll();
	}
	
	@Test
    public void getLaundriesReturnsErrorForNoIds() throws Exception {
        //Arrange
        Gson g = new Gson();
        ArrayList<Long> id = new ArrayList<Long>();
        when(laundryServiceMock.getLaundriesByIds(id)).thenReturn(null);
        //Act
        this.mvc.perform(put("/adminservice/laundries/")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(g.toJson(id)))
        	.andDo(MockMvcResultHandlers.print())
        //Assert
        	.andExpect(status().isBadRequest());
	}
	
	@Test
	public void setLaundryPaidForLaundryWithPaidUnequalTotalCostReturnsValidLaundry() throws Exception {
		//Arrange
		Gson g = new Gson();
		Laundry expectedLaundry = new Laundry(1);
		expectedLaundry.setPaid(BigDecimal.ZERO);
		expectedLaundry.setTotalCost(new BigDecimal(10));
		Mockito.when(laundryServiceMock.getLaundry((long) 1)).thenReturn(expectedLaundry);
		Mockito.when(laundryServiceMock.updateLaundry(expectedLaundry)).thenReturn(expectedLaundry);
		//Act
		String response = 
				this.mvc.perform(get("/adminservice/laundries/1/paid"))
		        .andDo(MockMvcResultHandlers.print())
		       	.andExpect(status().isOk())
		       	.andReturn().getResponse().getContentAsString();
		Laundry responseLaundry = g.fromJson(response, Laundry.class);
		//Assert
		assertThat(responseLaundry).isEqualToComparingFieldByField(expectedLaundry);
	}

}

