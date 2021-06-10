package com.adesso.digitalwash.repositories;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adesso.digitalwash.model.Laundry;

@Repository
public interface LaundryRepository extends CrudRepository<Laundry, Long> {

	public Collection<Laundry> findLaundriesByLaundryOwner(String laundryOwner);
	
	public Collection<Laundry> findByAcceptedDate(LocalDate acceptedDate);
}
