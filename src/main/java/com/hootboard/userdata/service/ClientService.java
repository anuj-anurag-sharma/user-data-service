package com.hootboard.userdata.service;

import com.hootboard.userdata.exception.LoginException;
import com.hootboard.userdata.exception.RegistrationException;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.request.RegistrationRequest;
import com.hootboard.userdata.response.Response;

public interface ClientService {

	Response registerClient(RegistrationRequest request) throws RegistrationException;

	void loginClient(LoginRequest login) throws LoginException;

}
