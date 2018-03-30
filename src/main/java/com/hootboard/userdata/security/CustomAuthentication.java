package com.hootboard.userdata.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthentication extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2139093710093926816L;

	private String token;

	private String clientId;

	public CustomAuthentication(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	public CustomAuthentication(String id) {
		super(null);
		this.token = id;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return clientId;
	}

	public String getToken() {
		return token;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
