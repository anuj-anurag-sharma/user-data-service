package com.hootboard.userdata.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hootboard.userdata.exception.AuthTokenException;
import com.hootboard.userdata.exception.LoginException;
import com.hootboard.userdata.exception.RegistrationException;
import com.hootboard.userdata.exception.RequestValidationException;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.request.RegistrationRequest;
import com.hootboard.userdata.response.AuthTokenResponse;
import com.hootboard.userdata.response.Response;
import com.hootboard.userdata.service.AuthService;
import com.hootboard.userdata.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

	private Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private ClientService clientSvc;

	@Autowired
	private AuthService authSvc;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Response> registerClient(@RequestBody @Valid RegistrationRequest request,
			BindingResult result) throws RegistrationException, RequestValidationException {
		LOGGER.info("registerClient, Request : {}", request);
		Response response = clientSvc.registerClient(request);
		if (result.hasErrors()) {
			throw new RequestValidationException(result);
		}
		LOGGER.info("registerClient, Response : {}", response);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/auth-token", method = RequestMethod.POST)
	public ResponseEntity<AuthTokenResponse> generateAuthToken(@RequestBody @Valid LoginRequest login,
			BindingResult result) throws LoginException, AuthTokenException, RequestValidationException {
		LOGGER.info("generateAuthToken, Request : {}", login);
		clientSvc.loginClient(login);
		if (result.hasErrors()) {
			throw new RequestValidationException(result);
		}
		AuthTokenResponse atr = authSvc.getAuthToken(login);
		LOGGER.info("generateAuthToken, Response : {}", atr);
		return new ResponseEntity<AuthTokenResponse>(atr, HttpStatus.OK);

	}

}
