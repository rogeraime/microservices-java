package com.adesso.digitalwash.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
	
	private RestTemplate rest;
	private String token;
	
	@Value("${notification.admin}")
	private String notificationAdmin;
	
	@Value("${notification.password}")
	private String notificationPassword;
	
	@Value("${authServer.schema}")
	private String authServerSchema;
	
	@Value("${authServer.host}")
	private String authServerHost;
	
	@Value("${authServer.port}")
	private String authServerPort;
	
	public AuthService() {
		this.rest = new RestTemplate();
	}

	public String getToken() {
		return token != null ? token : authenticate();
	}
	
	private String authenticate() {
		String oAuthPathAndQuery = "oauth/token?grant_type=password&username=" + notificationAdmin + "&password=" + notificationPassword;
		try {
			String responseJsonString = rest.postForEntity(getAuthServerUrl() + oAuthPathAndQuery, null, String.class).getBody();
			JSONObject responseJson = new JSONObject(responseJsonString);
			token = responseJson.getString("access_token");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return token;
	}
	
	private String getAuthServerUrl() {
		return authServerSchema + "://" + authServerHost + ":" + authServerPort + "/";
	}
}
