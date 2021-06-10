package com.adesso.digitalwash.model;

import java.math.BigDecimal;

public class Category {

	private Long id;
	private String name;
	private BigDecimal price;
	private boolean active = true;
	private BigDecimal priceCache;
	private boolean activeCache = this.active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/*-------------- Constructors ------------------*/
	public Category() {
	}

	public Category(String name, BigDecimal price, Long id) {
		this();
		this.id = id;
		this.name = name;
		this.price = price;
		this.priceCache = price;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public BigDecimal getPriceCache() {
		return priceCache;
	}

	public void setPriceCache(BigDecimal priceCache) {
		this.priceCache = priceCache;
	}

	public boolean isActiveCache() {
		return activeCache;
	}

	public void setActiveCache(boolean activeCache) {
		this.activeCache = activeCache;
	}
}
