package com.adesso.digitalwash.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.adesso.digitalwash.api.LaundryController;
import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Cloth;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.services.LaundryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(value = LaundryController.class)
public class LaundryControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LaundryService laundryServiceMock;

	String laundryOwner = "user";
	Long testId = 1L;
	Category categoryMock1 = new Category("Hemd", BigDecimal.ZERO, testId);
	Category categoryMock2 = new Category("Krawatte", new BigDecimal(3.57), 2L);

	// 3 Hemden
	Cloth cloth1 = new Cloth(3, BigDecimal.ZERO, 1L);
	// 2 Jacketts
	Cloth cloth2 = new Cloth(2, new BigDecimal(3.57), 2L);

//	@Test
//	public void getCategoriesTest() throws Exception {
//
//		String categoryMock1AsJson = objectMapper.writeValueAsString(categoryMock1);
//		String categoryMock2AsJson = objectMapper.writeValueAsString(categoryMock2);
//
//		when(laundryServiceMock.findAllCategories()).thenReturn(Arrays.asList(categoryMock1, categoryMock2));
//
//		String expected = "[" + categoryMock1AsJson + "," + categoryMock2AsJson + "]";
//
//		MockHttpServletResponse response = this.mockMvc.perform(get("/categories")).andDo(print()).andReturn()
//				.getResponse();
//
//		assertThat(response.getContentAsString()).isEqualTo(expected);
//	}

	@Test
	public void getLaundriesByLaundryOwnerTest() throws Exception {
		when(laundryServiceMock.findLaundriesByLaundryOwner(Mockito.anyString()))
				.thenReturn(Arrays.asList(new Laundry(Arrays.asList(cloth1))));
		this.mockMvc.perform(get("/laundries/" + laundryOwner)).andExpect(jsonPath("$", hasSize(1)));
	}

//	@Test
//	public void getCategoryByIdTest() throws Exception {
//
//		String categoryMock1AsJson = objectMapper.writeValueAsString(categoryMock1);
//
//		Mockito.when(laundryServiceMock.findCategoryById(Mockito.anyLong())).thenReturn(categoryMock1);
//
//		MockHttpServletResponse response = this.mockMvc
//				.perform(get("/category/" + testId).accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.id", is(testId.intValue()))).andExpect(jsonPath("$.name", is("Hemd")))
//				.andDo(print()).andReturn().getResponse();
//
//		assertThat(response.getContentAsString()).isEqualTo(categoryMock1AsJson);
//
//	}

	@Test
	public void cannotInsertIllegalValuesWithAddLaundry() throws Exception {

		cloth1.setId(1L);
		String cloth1AsJson = objectMapper.writeValueAsString(cloth1);
		cloth2.setId(2L);
		String cloth2AsJson = objectMapper.writeValueAsString(cloth2);

		// to test that no user can overwrite the paid-attribute
		String laundryWithWrongInput = "{\"paid\":\"true\",\"clothes\":[" + cloth1AsJson + "," + cloth2AsJson + "]}";

		MockHttpServletResponse response = this.mockMvc
				.perform(post("/create-laundry").accept(MediaType.APPLICATION_JSON).content(laundryWithWrongInput)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.paid", is(false))).andDo(print()).andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
	}
}
