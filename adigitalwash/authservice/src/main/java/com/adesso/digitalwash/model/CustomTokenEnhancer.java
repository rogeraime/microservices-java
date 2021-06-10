package com.adesso.digitalwash.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		try {
			Map<String, Object> additionalInfo = new HashMap<>();
			CustomUser userDetails = (CustomUser) authentication.getPrincipal();
			additionalInfo.put("user_full_name", userDetails.getCommonName());
			additionalInfo.put("user_eMail", userDetails.getEMail());
			ArrayList<String> roles = new ArrayList<>();
			roles.add("ROLE_USER");
			if (userDetails.getRoles().contains("FHDW-Studenten"))
				roles.add("ROLE_ADMIN");
			additionalInfo.put("authorities", roles);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).info("No additional Info added to Token");
		}
		return accessToken;
	}
}