package com.adesso.digitalwash.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cloth")
@Getter @Setter @NoArgsConstructor
public class Cloth {
	public Cloth(byte[] image) {
		this.image = image;
	}
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private @Id @Setter(AccessLevel.PROTECTED)long id;
	
	@Column(name = "color")
	private Color color;
	
	
	@Column(name = "category")
	private Category category;
	
	
	@Lob
	@Column(name = "image")
	private byte[] image;
}
