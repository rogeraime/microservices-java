package com.adesso.digitalwash.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.services.CategoryService;
import com.adesso.digitalwash.services.LaundryService;

@RestController
@RequestMapping("/categoryservice")
public class CategoryController {

	private CategoryService categoryService;
	private LaundryService laundryService;
	
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	@Autowired
	public CategoryController(CategoryService categoryService, LaundryService laundryService) {
		this.categoryService = categoryService;
		this.laundryService = laundryService;
		initPriceUpdateScheduling();
		initCategoryActiDeactivationScheduling();
	}
	
	private void initCategoryActiDeactivationScheduling() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = getThreadPoolTaskScheduler();
        threadPoolTaskScheduler.schedule(activateOrDeactivateCategory(), new CronTrigger(getCronExpression()));
	}

	private void initPriceUpdateScheduling() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = getThreadPoolTaskScheduler();
        threadPoolTaskScheduler.schedule(updatePrices(), new CronTrigger(getCronExpression()));
	}

	@GetMapping("/categories")
	public Collection<Category> getAllCategories() {
		return categoryService.findAllCategories();
	}

	@GetMapping("/{id}")
	public Category getCategoryById(@PathVariable Long id) {
		return categoryService.findCategoryById(id);
	}
	
	@GetMapping("/prices")
	public Collection<BigDecimal> getAllPrices() {
		Collection<BigDecimal> res = new ArrayList<>();
		getAllCategories().forEach(c -> res.add(c.getPrice()));
		return res;
	}
	
	@GetMapping("/categories/active")
	Collection<Category> getActiveCategories() {
		Collection<Category> allCategories = getAllCategories();
		ArrayList<Category> filteredCategories = new ArrayList<>();
		for (Category category : allCategories) {
			if (category.isActive()) {
				filteredCategories.add(category);
			}
		}
		return filteredCategories;
	}

	@PutMapping("/update")
	public Category[] updateCategories(@RequestBody Category categories[]) {
		for (int i = 0; i < categories.length; i++) {
			categories[i] = categoryService.createOrUpdateCategory(categories[i]);
		}
		return categories;
	}
	
	protected Runnable updatePrices() {
		return () -> {
			Collection<Category> categories = categoryService.findAllCategories();
			for (Category category : categories) {
				if (category.getPrice().compareTo(category.getPriceCache()) != 0) {
					category.setPrice(category.getPriceCache());
					categoryService.createOrUpdateCategory(category);
				}
			}
		};
	}
	
	protected Runnable activateOrDeactivateCategory() {
		return() -> {
			Collection<Category> categories = categoryService.findAllCategories();
			for(Category category : categories) {
				if(category.isActive() != category.isActiveCache()) {
					category.setActive(category.isActiveCache());
					categoryService.createOrUpdateCategory(category);
				}
			}
		};
	}
	
	private String getCronExpression() {
		String cronStringWithoutWeekDays = "0 0 0 ? * ";
		String weekDaysToAppendForCron = StringUtils.join(laundryService.getWeekdays(), ",");
		
		String cronComplete = cronStringWithoutWeekDays + weekDaysToAppendForCron;
		
		return cronComplete;
	}
	
	private ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
		if (threadPoolTaskScheduler != null) {
			return threadPoolTaskScheduler;
		} else {
			threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
			threadPoolTaskScheduler.setPoolSize(1);
			threadPoolTaskScheduler.initialize();
		}
		return threadPoolTaskScheduler;
	}
}
