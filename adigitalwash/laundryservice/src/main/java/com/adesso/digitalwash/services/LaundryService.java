package com.adesso.digitalwash.services;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adesso.digitalwash.model.Cloth;
import com.adesso.digitalwash.model.Laundry;
import com.adesso.digitalwash.repositories.ClothRepository;
import com.adesso.digitalwash.repositories.LaundryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LaundryService {

	@Autowired
	private LaundryRepository laundryRepository;

	@Autowired
	private ClothRepository clothRepository;

	///////////////////////////////////////////////////////////////////
	// Methods for laundry
	public void addLaundry(Laundry laundry) {
		laundryRepository.save(laundry);
	}

	public void updateLaundry(Laundry laundry) {
		laundryRepository.save(laundry);
	}

	public Laundry getLaundryById(Long id) {
		return laundryRepository.findById(id).get();
	}

	public void deleteLaundryWithId(Long id) {
		laundryRepository.deleteById(id);
	}

	public Collection<Laundry> findAllLaundries() {
		Collection<Laundry> laundries = new ArrayList<>();
		laundryRepository.findAll().forEach(l -> laundries.add(l));
		return laundries;
	}

	public Collection<Laundry> getCurrentLaundries() {
		Collection<Laundry> laundries = new ArrayList<>();
		laundryRepository.findByAcceptedDate(LocalDate.now(ZoneId.of("UTC"))).forEach(l -> laundries.add(l));
		return laundries;
	}

	// Methods for clothes
	public void createCloth(Cloth cloth) {
		clothRepository.save(cloth);
	}

	public void deleteClothWithId(Long id) {
		clothRepository.deleteById(id);
	}

	public Collection<Cloth> getClothesWithLaundryId(Long id) {
		Collection<Cloth> clothes = new ArrayList<Cloth>();
		clothRepository.findAll().forEach(c -> {
			if (c.getLaundry().getId() == id)
				clothes.add(c);
		});
		return clothes;
	}

	public Collection<Cloth> findAllClothes() {
		Collection<Cloth> clothes = new ArrayList<>();
		clothRepository.findAll().forEach(c -> clothes.add(c));
		return clothes;
	}

	public <T> T readValues(String stream, Class<T> obj) {
		ObjectMapper m = new ObjectMapper();
		try {
			return m.readValue(stream, obj);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public Collection<Laundry> findLaundriesByLaundryOwner(String laundryOwner) {
		Collection<Laundry> laundries = new ArrayList<>();
		laundryRepository.findLaundriesByLaundryOwner(laundryOwner).forEach(l -> laundries.add(l));
		return laundries;
	}
}
