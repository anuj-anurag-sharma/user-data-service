package com.hootboard.userdata.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hootboard.userdata.exception.NotFoundException;
import com.hootboard.userdata.exception.RequestValidationException;
import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.Response;
import com.hootboard.userdata.service.UserService;

@RestController
@RequestMapping("/secure/user")
public class UserController {

	@Autowired
	private UserService userSvc;

	private Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Response> createUser(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult)
			throws UserException, RequestValidationException {
		LOGGER.info("createUser, Request : {}", userRequest);
		if (bindingResult.hasErrors()) {
			throw new RequestValidationException(bindingResult);
		}
		Response user = userSvc.createUser(userRequest);
		LOGGER.info("createUser, Response :  {}", user);
		return new ResponseEntity<Response>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Response> updateUser(@PathVariable String id, @RequestBody @Valid UserRequest userRequest,
			BindingResult bindingResult) throws UserException, RequestValidationException, NotFoundException {
		LOGGER.info("updateUser, Request for {} : {}", id, userRequest);
		if (bindingResult.hasErrors()) {
			throw new RequestValidationException(bindingResult);
		}
		Response user = userSvc.updateUser(id, userRequest);
		LOGGER.info("updateUser, Response for {} : {}", id, user);
		return new ResponseEntity<Response>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Response> getUser(@PathVariable String id) throws NotFoundException {
		LOGGER.info("getUser, Request for {}", id);
		Response user = userSvc.getUser(id);
		LOGGER.info("getUser, Response for {} : {}", id, user);
		return new ResponseEntity<Response>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Response> deleteUser(@PathVariable String id) throws NotFoundException {
		LOGGER.info("getUser, Request for {}", id);
		Response response = userSvc.deleteUser(id);
		LOGGER.info("getUser, Response for {} : {}", id, response);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Response>> getAllUsers() throws UserException {
		LOGGER.info("getAllUsers, Request ");
		List<Response> users = userSvc.getAllUsers();
		LOGGER.info("getAllUsers, Response : {} ", users);
		return new ResponseEntity<List<Response>>(users, HttpStatus.OK);
	}

}
