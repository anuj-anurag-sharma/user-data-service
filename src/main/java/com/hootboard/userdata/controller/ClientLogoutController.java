package com.hootboard.userdata.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hootboard.userdata.exception.AuthTokenException;
import com.hootboard.userdata.response.MessageResponse;
import com.hootboard.userdata.response.Response;
import com.hootboard.userdata.service.AuthService;

@RestController
@RequestMapping("/secure")
public class ClientLogoutController {

	private Logger LOGGER = LoggerFactory.getLogger(ClientLogoutController.class);

	@Autowired
	private AuthService authSvc;

	@RequestMapping(value = "/client/logout", method = RequestMethod.POST)
	public ResponseEntity<Response> invalidateAuthToken() throws AuthTokenException {
		LOGGER.info("invalidateAuthToken, Request");
		MessageResponse atr = authSvc.invalidateAuthToken();
		LOGGER.info("invalidateAuthToken, Response : {}", atr);
		return new ResponseEntity<Response>(atr, HttpStatus.OK);

	}

}
