package com.adesso.digitalwash;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String AUTHORITY_DASHBOARD = "VIEW";
	private static final String AUTHORITY_MICROSERIVCE = "CONNECT";
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
				.withUser("microservice").password("thisISthEconnectION2Eureka").authorities(WebSecurityConfig.AUTHORITY_MICROSERIVCE).and()
				.withUser("eurekaAdmin").password("@EUReka2019").authorities(WebSecurityConfig.AUTHORITY_DASHBOARD);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
			//Setup for Dashboard
			.antMatchers("/eureka/css/**", "/eureka/js/**", "/eureka/fonts/**", "/eureka/images/**").hasAuthority(WebSecurityConfig.AUTHORITY_DASHBOARD)
			.antMatchers("/eureka/**").hasAnyAuthority(WebSecurityConfig.AUTHORITY_MICROSERIVCE)
			.anyRequest().hasAuthority(WebSecurityConfig.AUTHORITY_DASHBOARD).and().httpBasic();
	}
}
