package com.adesso.digitalwash.model;

public enum Color {
	Gelb,
	GelbOrange,
	Orange,
	RotOrange,
	Rot,
	RotViolett,
	Violett,
	BlauViolett,
	Blau,
	BlauGrün,
	Grün,
	GelbGrün,
	Weiß,
	Schwarz,
	Grau,
	Beige;
	
	public static Color valueOf(int value) {
    	return Color.values()[value];
    }
}
