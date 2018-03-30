package com.hootboard.userdata.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hootboard.userdata.exception.AuthTokenException;
import com.hootboard.userdata.exception.LoginFailureException;
import com.hootboard.userdata.exception.RegistrationException;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.request.RegistrationRequest;
import com.hootboard.userdata.response.AuthTokenResponse;
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
	public ResponseEntity<String> registerClient(@RequestBody RegistrationRequest request)
			throws RegistrationException {
		String clientId = clientSvc.registerClient(request);
		return new ResponseEntity<String>(clientId, HttpStatus.OK);

	}

	@RequestMapping(value = "/auth-token", method = RequestMethod.POST)
	public ResponseEntity<AuthTokenResponse> generateAuthToken(@RequestBody LoginRequest login)
			throws LoginFailureException, AuthTokenException {

		clientSvc.loginClient(login);

		AuthTokenResponse atr;
		try {
			atr = authSvc.getAuthToken(login);
			return new ResponseEntity<AuthTokenResponse>(atr, HttpStatus.OK);
		} catch (AuthTokenException e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<AuthTokenResponse>(HttpStatus.FORBIDDEN);
		}

	}

}
