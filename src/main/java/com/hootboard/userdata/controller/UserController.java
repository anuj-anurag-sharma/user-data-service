package com.hootboard.userdata.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.UserResponse;
import com.hootboard.userdata.service.UserService;

@RestController
@RequestMapping("/secure/user")
public class UserController {

	@Autowired
	private UserService userSvc;

	// private Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest,
			BindingResult bindingResult) throws UserException {
		if (bindingResult.hasErrors()) {
			List<String> message = formErrorMessage(bindingResult);
			throw new UserException(message.toString());
		}
		UserResponse user = userSvc.createUser(userRequest);
		return new ResponseEntity<UserResponse>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserRequest userRequest, BindingResult bindingResult)
			throws UserException {
		if (bindingResult.hasErrors()) {
			List<String> message = formErrorMessage(bindingResult);
			throw new UserException(message.toString());
		}
		UserResponse user = userSvc.updateUser(id, userRequest);
		return new ResponseEntity<UserResponse>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserResponse> getUser(@PathVariable String id) throws UserException {
		UserResponse user = userSvc.getUser(id);
		return new ResponseEntity<UserResponse>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteUser(@PathVariable String id) throws UserException {
		Boolean bool = userSvc.deleteUser(id);
		return new ResponseEntity<Boolean>(bool, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<UserResponse>> getAllUsers() throws UserException {
		List<UserResponse> users = userSvc.getAllUsers();
		return new ResponseEntity<List<UserResponse>>(users, HttpStatus.OK);
	}

	private List<String> formErrorMessage(BindingResult bindingResult) {
		List<FieldError> errors = bindingResult.getFieldErrors();
		List<String> message = new ArrayList<>();
		for (FieldError e : errors) {
			message.add("@" + e.getField() + ":" + e.getDefaultMessage());
		}
		return message;
	}
}
