package com.adesso.digitalwash.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adesso.digitalwash.model.Laundry;

@Repository
public interface LaundryRepository extends CrudRepository<Laundry, Long> {

	public List<Laundry> findByIdIn(Collection<Long> ids);

	public List<Laundry> findByAcceptedDateBetween(LocalDate start, LocalDate end);
}
