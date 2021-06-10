package com.adesso.digitalwash.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Cloth;
import com.adesso.digitalwash.model.Color;

@Repository
public interface ClothRepository extends CrudRepository<Cloth, Long> {

	public List<Cloth> findAllByColorAndCategory(Color color, Category category);
	public List<Cloth> findAllByColor(Color color);
	public List<Cloth> findAllByCategory(Category category);
}
