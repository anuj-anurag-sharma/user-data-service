package com.hootboard.userdata.service;

import com.hootboard.userdata.exception.AuthTokenException;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.response.AuthTokenResponse;
import com.hootboard.userdata.response.MessageResponse;

public interface AuthService {

	AuthTokenResponse getAuthToken(LoginRequest login) throws AuthTokenException;

	MessageResponse invalidateAuthToken() throws AuthTokenException;

}
