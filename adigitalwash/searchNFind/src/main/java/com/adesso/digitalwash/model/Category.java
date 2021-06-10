package com.adesso.digitalwash.model;

public enum Category {
	Hemd_Bluse,
	Krawatte,
	Jackett,
	Anzug,
	Hose,
	T_Shirt,
	Pullover;
	
    public static Category valueOf(int value) {
    	return Category.values()[value];
    }
}
