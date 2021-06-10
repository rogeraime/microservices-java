package com.adesso.digitalwash.api;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adesso.digitalwash.model.Category;
import com.adesso.digitalwash.model.Cloth;
import com.adesso.digitalwash.model.Color;
import com.adesso.digitalwash.repository.ClothRepository;

@RestController
@RequestMapping("/searchNFind")
@Transactional
public class SnFController {
	
	@Autowired
	ClothRepository clothRepository;
	
	/*@Autowired
	TagGenerator tagGenerator;
	*/
	
	@PostMapping("/image/{image}")
	public ResponseEntity<HttpStatus> setCloth(@PathVariable(value = "image") String image) {
		Cloth cloth = decodeImageToCloth(image);
		generateTags(cloth);
		cloth = clothRepository.save(cloth);
		return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}

	@PostMapping({"/image/{image}/color/{color}/category/{category}", "/image/{image}/category/{category}/color/{color}"})
	public ResponseEntity<HttpStatus> setClothWihKeywords(@PathVariable(value = "image") String image, @PathVariable(value = "color") Color color, @PathVariable(value = "category") Category category) {
		Cloth cloth = decodeImageToCloth(image);
		cloth.setCategory(category);
		cloth.setColor(color);
		cloth = clothRepository.save(cloth);
		return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
	}
	
	private Cloth decodeImageToCloth(String image) {
		byte[] decodedImage = Base64.getDecoder().decode(image);
		return new Cloth(decodedImage);
	}
	
	@Async
	public void generateTags(Cloth cloth) {
		//TODO Use CNN Model to generate Tags
		throw new NotImplementedException("KI is not working now.");
	}
	
	@GetMapping("/color/{color}")
	public ResponseEntity<?> getClothByColor(@PathVariable(value = "color") Color color) {
		List<Cloth> clothes = clothRepository.findAllByColor(color);
		if(clothes.size() == 0) return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<String>>(encodeClothImages(clothes), HttpStatus.OK);
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<?> getClothByCategory(@PathVariable(value = "category") Category category) {
		List<Cloth> clothes = clothRepository.findAllByCategory(category);
		if(clothes.size() == 0) return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<String>>(encodeClothImages(clothes), HttpStatus.OK);
	}
	
	@GetMapping({"/category/{category}/color/{color}", "/color/{color}/category/{category}"})
	public ResponseEntity<?> getClothByCategoryAndColor(@PathVariable(value = "color") Color color, @PathVariable(value = "category") Category category) {
		List<Cloth> clothes = clothRepository.findAllByColorAndCategory(color, category);
		if(clothes.size() == 0) return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<String>>(encodeClothImages(clothes), HttpStatus.OK);
	}
	
	private List<String> encodeClothImages(List<Cloth> clothes) {
		List<String> images = new ArrayList<String>();
		Base64.Encoder encoder = Base64.getEncoder();
		for(Cloth cloth : clothes) {
			images.add(encoder.encodeToString(cloth.getImage()));
		}
		return images;
	}

}
