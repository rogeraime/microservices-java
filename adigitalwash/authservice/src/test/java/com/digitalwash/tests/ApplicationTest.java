package com.digitalwash.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.test.context.junit4.SpringRunner;

import com.adesso.digitalwash.AuthserviceApplication;

@EnableAutoConfiguration
@SpringBootTest(classes={AuthserviceApplication.class})
@RunWith(SpringRunner.class)
public class ApplicationTest {
	
	@Autowired
	private LdapTemplate ldapTemplate;
	@Autowired
	private LdapAuthenticationProvider authProvider;
	
	@Test
	public void authProviderLoaded() throws Exception {
		assertNotNull(authProvider);
	}
	
	@Test
	public void ldapTemplateTest() throws Exception {
		assertNotNull(ldapTemplate);
	}
	@Test
	public void ldapTemplateLoginTest() throws Exception {
		Filter filter = new EqualsFilter("uid", "user");
		
		boolean loggedIn = ldapTemplate.authenticate("", filter.toString(), "pwd");
		assertTrue(loggedIn);
	}
}
