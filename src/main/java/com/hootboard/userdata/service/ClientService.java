package com.hootboard.userdata.service;

import com.hootboard.userdata.exception.LoginFailureException;
import com.hootboard.userdata.exception.RegistrationException;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.request.RegistrationRequest;

public interface ClientService {

	String registerClient(RegistrationRequest request) throws RegistrationException;

	Boolean loginClient(LoginRequest login) throws LoginFailureException;

}
