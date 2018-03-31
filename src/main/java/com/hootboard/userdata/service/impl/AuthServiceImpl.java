package com.hootboard.userdata.service.impl;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hootboard.userdata.entity.AuthToken;
import com.hootboard.userdata.exception.AuthTokenException;
import com.hootboard.userdata.repo.AuthTokenRepo;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.response.AuthTokenResponse;
import com.hootboard.userdata.response.MessageResponse;
import com.hootboard.userdata.security.CustomAuthentication;
import com.hootboard.userdata.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthTokenRepo authTokenRepo;

	private Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Override
	public AuthTokenResponse getAuthToken(LoginRequest login) throws AuthTokenException {
		LOGGER.info("Generating token for " + login.getClientId());
		DateTime now = DateTime.now();
		try {
			AuthToken token = authTokenRepo.save(new AuthToken(login.getClientId(), now));
			AuthTokenResponse atr = new AuthTokenResponse();
			atr.setToken(token.getAuthToken());
			atr.setGeneratedOn(now.toString());
			LOGGER.info("Generated token: " + token + " for " + login.getClientId());
			return atr;
		} catch (Exception e) {
			throw new AuthTokenException(e);
		}
	}

	@Override
	public MessageResponse invalidateAuthToken() throws AuthTokenException {

		try {
			CustomAuthentication authentication = (CustomAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			authTokenRepo.deleteById(authentication.getToken());
			MessageResponse response = new MessageResponse();
			response.setMsg("Auth Toke invalidated");
			return response;
		} catch (Exception e) {
			throw new AuthTokenException(e);
		}
	}

}
