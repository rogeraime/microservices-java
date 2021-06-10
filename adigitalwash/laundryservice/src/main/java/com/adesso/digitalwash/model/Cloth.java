package com.adesso.digitalwash.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cloth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer clothCount;
	private BigDecimal costs;

//	@ManyToOne
//	private Category category;
	private Long categoryId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Laundry laundry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getClothCount() {
		return clothCount;
	}

	public void setClothCount(Integer clothCount) {
		this.clothCount = clothCount;
	}

	public BigDecimal getCosts() {
		return costs;
	}

	public void setCosts(BigDecimal costs) {
		this.costs = costs;
	}

//	public Category getCategory() {
//		return category;
//	}
	
	public Long getCategoryId() {
		return categoryId;
	}

//	public void setCategory(Category category) {
//		this.category = category;
//	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public Laundry getLaundry() {
		return laundry;
	}

	public void setLaundry(Laundry laundry) {
		this.laundry = laundry;
	}

	/** resolves the Bidirectional Problem */
	public void assignLaundry(Laundry laundry) {
		if (this.laundry != null && laundry != null) {
			this.laundry = laundry;
		}
	}

//	public Cloth computCost() {
//		if (!(clothCount == 0 || category == null)) {
//			this.costs =category.getPrice().multiply( new BigDecimal(clothCount));
//		}
//		return this;
//	}

	/*-------------- Constructors ------------------*/
	public Cloth() {
		super();
	}

//	public Cloth(Integer clothCount) {
//		this();
//		this.clothCount = clothCount;
//	}
	
	public Cloth(Integer clothCount, BigDecimal costs, Long categoryId) {
		this();
		this.clothCount = clothCount;
		this.costs = costs;
		this.categoryId = categoryId;
	}
}
