package com.adesso.digitalwash.repositories;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.adesso.digitalwash.model.Category;

@Component
public class CategoryCommandLineRunner implements CommandLineRunner {

	@Autowired
	private CategoryRepository repository;

	@Override
	public void run(String... arg0) throws Exception {
		repository.save(new Category("Hemd/ Bluse", new BigDecimal(0.0), new Long(1)));
		repository.save(new Category("Krawatte", new BigDecimal(3.57), new Long(2)));
		repository.save(new Category("Jackett", new BigDecimal(5.00), new Long(3)));
		repository.save(new Category("Anzug", new BigDecimal(7.97), new Long(4)));
		repository.save(new Category("Hose", new BigDecimal(2.97), new Long(5)));
		repository.save(new Category("T-Shirt", new BigDecimal(3.10), new Long(6)));
		repository.save(new Category("Pullover", new BigDecimal(4.20), new Long(7)));
	}
}
