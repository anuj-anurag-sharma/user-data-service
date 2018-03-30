package com.hootboard.userdata.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.hootboard.userdata.entity.AuthToken;
import com.hootboard.userdata.repo.AuthTokenRepo;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AuthTokenRepo authTokenRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomAuthentication ca = (CustomAuthentication) authentication;
		Optional<AuthToken> record = authTokenRepo.findById(ca.getToken());
		if (!record.isPresent()) {
			throw new BadCredentialsException("Auth token is not valid");
		}
		ca.setClientId(record.get().getClientId());
		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
