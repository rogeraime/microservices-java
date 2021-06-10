package com.adesso.digitalwash.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LaundryService {
	@Value("${protocol.http}")
	private String http;
	
	@Value("${server.ip}")
	private String serverIp;
	
	@Value("${server.allowedOrigin}")
	private String port;
	
	@Value("${services.laundryservice}")
	private String service;
	
	@Value("${rest.weekdays}")
	private String restMappingName;
	
	public Collection<Integer> getWeekdays(){
		Collection<Integer> weekDays = new ArrayList<>();
		
		RestTemplate restTemplate = new RestTemplate();
		weekDays = restTemplate.getForObject(getWeekDaysRestUrl(), Collection.class);

		return weekDays;
	}
	
	private String getWeekDaysRestUrl() {
		return http + serverIp + ":" + port + "/" + service + "/" + restMappingName;
	}
}
