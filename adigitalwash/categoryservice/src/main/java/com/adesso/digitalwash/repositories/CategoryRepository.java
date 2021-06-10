package com.adesso.digitalwash.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adesso.digitalwash.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}
