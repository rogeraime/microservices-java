package com.adesso.digitalwash.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.Optional;


public class Laundry {


	private Long id;

	private LocalDate submissionDate;
	private LocalDate acceptedDate;
	private LocalDate deliveryDate;

	private BigDecimal paid;

	// fetchedByCleaners is set by the admin when the cleaners fetched all laundry
	private Boolean fetchedByCleaners = false;
	// fetchedByOwner is set by the owner when he fetched his clean laundry
	private Boolean fetchedByOwner = false;
	private String laundryOwner;
	private BigDecimal totalCost;
	private String totalCostOutputString;
	private String image;

	/* A laundry contains many clothes */
	private List<Cloth> clothes = new ArrayList<Cloth>();
	
	public Integer getClothCountOfCategory(Long id) {
		Optional <Cloth> c = this.clothes.stream().filter(cl -> cl.getCategoryId().equals(id)).findFirst();
		return c.isPresent() ? c.get().getClothCount() : 0;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Boolean getFetchedByCleaners() {
		return fetchedByCleaners;
	}

	public void setFetchedByCleaners(Boolean fetchedByCleaners) {
		this.fetchedByCleaners = fetchedByCleaners;
	}

	public Boolean getFetchedByOwner() {
		return fetchedByOwner;
	}

	public void setFetchedByOwner(Boolean fetchedByOwner) {
		this.fetchedByOwner = fetchedByOwner;
	}

	public LocalDate getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(LocalDate acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public String getLaundryOwner() {
		return laundryOwner;
	}

	public void setLaundryOwner(String laundryOwner) {
		this.laundryOwner = laundryOwner;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate returnDate) {
		this.deliveryDate = returnDate;
	}

	public LocalDate getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(LocalDate submissionDate) {
		this.submissionDate = submissionDate;
	}

	public List<Cloth> getClothes() {
		return clothes;
	}

	public void setClothes(List<Cloth> clothes) {
		this.clothes.addAll(clothes);
	}

	public void setCloth(Cloth cloth) {
		this.clothes.add(cloth);
	}

	public String getTotalCostOutputString() {
		return totalCostOutputString;
	}

	public void setTotalCostOutputString(BigDecimal totalCost) {
		this.totalCostOutputString = DecimalFormat.getInstance(Locale.GERMANY).format(totalCost);
	}
	
	public void setTotalCostOutputString(String totalCost) {
		this.totalCostOutputString = totalCost;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String img) {
		this.image = img;
	}


	/** resolves the Bidirectional Problem */
	public void assignClothesToLaundry(List<Cloth> clothes) {
		if (clothes != null) {
			clothes.forEach(c -> {
				assignClothToLaundry(c);
			});
		}
	}

	/** resolves the Bidirectional Problem */
	public void assignClothToLaundry(Cloth cloth) {
		if (cloth != null) {
			if (cloth.getLaundry() != this)
				cloth.setLaundry(this);
		}
	}

	public Laundry computeTotalCost() {
		if (!(clothes == null || clothes.isEmpty() || totalCost == null)) {
			clothes.forEach(c -> this.totalCost = totalCost.add(c.getCosts()));
		}
		this.setTotalCostOutputString(this.totalCost);
		return this;
	}

	public Laundry() {
		this.totalCost = BigDecimal.ZERO;
	}

	public Laundry(List<Cloth> clothes) {
		this.clothes = clothes;
	}
	public BigDecimal getPaid() {
		return paid;
	}
	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}
}
