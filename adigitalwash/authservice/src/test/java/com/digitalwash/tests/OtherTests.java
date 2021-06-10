package com.digitalwash.tests;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class OtherTests {
	
	@Test
	public void regularExpression() {
		
		Pattern pattern = Pattern.compile("^webjars/[a-zA-Z0-9/\\.]+(\\.[a-z]+)$");
		boolean matched = false;
		
		String url = "webjars/tether/1.4.0/dist/css/tether.min.css";
		Matcher matcher = pattern.matcher(url);
//		if (matcher.find()) {
//				matched = true;
//		}	
		if(url.matches("^webjars/[a-zA-Z0-9/\\.]+(\\.[a-z]+)$")){
			matched=true;
			System.out.println("Matched");
		}
		assertTrue(matched);
	}
}
