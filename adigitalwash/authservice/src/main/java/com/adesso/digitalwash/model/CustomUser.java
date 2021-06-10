package com.adesso.digitalwash.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {

	private String commonName;
	private String eMail;
	private String roles;

	public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			String commonName, String eMail, String roles) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.commonName = commonName;
		this.eMail = eMail;
		this.roles = roles;
	}

	public String getCommonName() {
		return commonName;
	}

	public String getEMail() {
		return eMail;
	}
	
	public String getRoles() {
		return roles;
	}
}