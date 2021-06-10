package com.adesso.digitalwash;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@EnableResourceServer
@Profile("test")
public class ResourceServerTestConfig extends ResourceServerConfigurerAdapter {
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.cors().disable().authorizeRequests().antMatchers("/**").permitAll();
		}
}