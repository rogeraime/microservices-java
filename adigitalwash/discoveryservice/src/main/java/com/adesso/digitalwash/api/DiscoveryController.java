package com.adesso.digitalwash.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscoveryController {
	@RequestMapping("/home")
	public String home() {
		return "Registry Running";
	}
}
