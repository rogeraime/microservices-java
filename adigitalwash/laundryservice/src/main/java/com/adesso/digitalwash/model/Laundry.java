package com.adesso.digitalwash.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import com.adesso.digitalwash.model.Cloth;

@Entity
public class Laundry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate submissionDate;
	private LocalDate acceptedDate;
	private LocalDate deliveryDate;
	
	private String laundryOwner;
	private BigDecimal totalCost;
	private BigDecimal paid;
	private String eMail;
	private boolean completed;
	
	@Lob
	private String image;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "laundry")
	private List<Cloth> clothes = new ArrayList<>();
	
	public Laundry() {
		this.totalCost = BigDecimal.ZERO;
	}

	public Laundry(List<Cloth> clothes) {
		this.clothes = clothes;
	}

	public Integer getClothCountOfCategory(Long id) {
		Optional<Cloth> c = this.clothes.stream().filter(cl -> cl.getCategoryId().equals(id)).findFirst();
		return c.isPresent() ? c.get().getClothCount() : 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(LocalDate submissionDate) {
		this.submissionDate = submissionDate;
	}

	public LocalDate getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(LocalDate acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public List<Cloth> getClothes() {
		return clothes;
	}

	public void setClothes(List<Cloth> clothes) {
		this.clothes = clothes;
	}

	public void computeTotalCost() {
		if (!(clothes == null || clothes.isEmpty() || totalCost == null)) {
			clothes.forEach(c -> totalCost = totalCost.add(c.getCosts()));
		}
	}

	public void assignClothesToLaundry(List<Cloth> clothes) {
		if (clothes != null) {
			clothes.forEach(c -> {
				assignClothToLaundry(c);
			});
		}
	}

	public void assignClothToLaundry(Cloth cloth) {
		if (cloth != null) {
			if (cloth.getLaundry() != this) {
				cloth.setLaundry(this);
			}
		}
	}

	public String geteMail() {
		return eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
