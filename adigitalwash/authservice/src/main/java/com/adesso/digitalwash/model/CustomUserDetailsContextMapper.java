package com.adesso.digitalwash.model;

import java.util.Collection;

import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class CustomUserDetailsContextMapper implements UserDetailsContextMapper {

	CustomUser userDetails;
	
	@Value("${ldap.user.nameAttribute}")
	private String nameAttribute;
	
	@Value("${ldap.user.roleAttribute}")
	private String roleAttribute;
	
	@Value("${ldap.user.mailAttribute}")
	private String mailAttribute;
	
	public CustomUserDetailsContextMapper() {
	}

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		String commonName = null;
		String eMail = null;
		String role = null;
		
		Attributes attributes = ctx.getAttributes();
		try {
			commonName = (String) attributes.get(nameAttribute).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			eMail = (String) attributes.get("mail").get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			role = (String) attributes.get(roleAttribute).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		userDetails = new CustomUser(username, "", true, true, true, true, authorities, commonName, eMail, role);
		return userDetails;
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
	}

	public CustomUser getUserDetails() {
		return this.userDetails;
	}

}