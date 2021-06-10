package com.adesso.digitalwash;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.adesso.digitalwash.model.Cloth;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DataJpaTest
public class LaundryControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CategoryService categoryService;

	@Before
	public void prepareTestData() {
		List<Cloth> clothes = Arrays.asList(new Cloth[] { new Cloth(2, new BigDecimal(10.0), (long)4) });
		Laundry l = new Laundry(clothes);
		entityManager.persistAndFlush(l);
	}

	// example for testing with embedded db
	@Test
	public void saveShouldPersistData() throws Exception {
		List<Cloth> clothes = Arrays.asList(new Cloth[] { new Cloth(2, new BigDecimal(10.0), (long)4) });
		Laundry l = new Laundry(clothes);
		Laundry laundry = this.entityManager.persistFlushFind(new Laundry(clothes));
		assertThat(laundry.getTotalCost()).isEqualTo(l.getTotalCost());
	}

	@Test
	public void getAllLaundries() throws Exception {
		this.mockMvc.perform(get("/laundries").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	public void getLaundry() throws Exception {
		this.mockMvc.perform(get("/laundries/latest")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(38));
	}

	@Test
	public void getLaundriesByDate() throws Exception {
		this.mockMvc
				.perform(get("/laundries/date/2017-12-04")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$", hasSize(38)));
	}

	@Test
	public void getUnpaidLaundries() throws Exception {
		this.mockMvc
				.perform(get("/laundries/unpaid").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$", hasSize(38)));
	}

	@Test
	public void getLaundryById() throws Exception {
		int id = 35;

		this.mockMvc.perform(get("/laundries/id/" + id)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id));
	}

	@Test
	public void addLaundry() throws Exception {
		List<Cloth> clothes = Arrays.asList(new Cloth[] { new Cloth(2, new BigDecimal(5.0), 1L) });
		Laundry laundry = new Laundry(clothes);

		// why is this not working??
		ObjectMapper mapper = new ObjectMapper();
		String jsonContent = mapper.writeValueAsString(laundry);

		String jsonLaundry = "{\r\n" + "	\"clothes\": [\r\n" + "		{\r\n" + "			\"clothCount\": 3,\r\n"
				+ "			\"costs\": 0,\r\n" + "			\"categoryId\": 1\r\n" + "		},\r\n" + "		{\r\n"
				+ "			\"clothCount\": 2,\r\n" + "			\"costs\": 7.14,\r\n"
				+ "			\"categoryId\": 2\r\n" + "		}\r\n" + "		]\r\n" + "}";

		this.mockMvc
				.perform(post("/laundries").content(jsonLaundry).contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.clothes[0].category.name").value("Hemd"));
	}

}
