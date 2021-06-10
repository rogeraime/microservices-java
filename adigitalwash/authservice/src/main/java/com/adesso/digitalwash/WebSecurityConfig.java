package com.adesso.digitalwash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import com.adesso.digitalwash.model.CustomUserDetailsContextMapper;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	//ldap Config
	@Value("${ldap.baseDn}")
	private String baseDn; // dc=adesso,dc=local

	@Value("${ldap.user.namePattern}")
	private String userDnPattern; // sAMAccountName={0}

	@Value("${ldap.server}")
	private String url; // ldap://ldap01.adesso.local:3268/

	@Value("${ldap.bindDn}")
	private String bindDn; // ldapuser

	@Value("${ldap.password}")
	private String userPassword; // password

	//notification userdata
	@Value("${notification.admin}")
	private String notificationAdmin;
	@Value("${notification.password}")
	private String notificationPassword;
	@Value("${notification.role}")
	private String notificationRole;
	
	@Value("${authenticate.user}")
	private String authenticateUser;
	@Value("${authenticate.password}")
	private String authenticatePassword;
	@Value("${authenticate.role}")
	private String authenticateRole;
	
	
	// Einstellung des Filters für das LDAP System
	@Autowired
	public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(notificationAdmin).password(notificationPassword).roles(notificationRole);
		auth.inMemoryAuthentication().withUser(authenticateUser).password(authenticatePassword).roles(authenticateRole);
		auth.ldapAuthentication().userSearchFilter(userDnPattern).userDetailsContextMapper(userDetailsContextMapper())
				.contextSource(contextSource());
	}

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll().antMatchers("/**").authenticated();
	}

	@Bean
	public CustomUserDetailsContextMapper userDetailsContextMapper() {
		return new CustomUserDetailsContextMapper();
	}

	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		DefaultSpringSecurityContextSource context = new DefaultSpringSecurityContextSource(url + baseDn);
		context.setUserDn(bindDn);
		context.setPassword(userPassword);
		context.afterPropertiesSet();
		return context;
	}
	
	
}
