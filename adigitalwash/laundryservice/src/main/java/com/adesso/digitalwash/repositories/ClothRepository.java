package com.adesso.digitalwash.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adesso.digitalwash.model.Cloth;

@Repository
public interface ClothRepository extends CrudRepository<Cloth, Long> {

}
