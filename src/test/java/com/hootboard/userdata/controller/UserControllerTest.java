package com.hootboard.userdata.controller;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.UserResponse;
import com.hootboard.userdata.service.UserService;

public class UserControllerTest {

	@Mock
	UserService userService;

	UserController userController;

	@Before
	public void setup() {
		userController = new UserController();
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(userController, "userSvc", userService);
	}

	@Test
	public void testUserCreate() throws UserException {
		UserResponse ur = new UserResponse();
		ur.setFirstName("FirstName");
		Mockito.when(userService.createUser(Mockito.any())).thenReturn(ur);

		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		req.setLastName("LastName");
		Set<String> emails = new HashSet<String>();
		emails.add("email@email.com");
		req.setEmails(emails);
		Set<String> addresses = new HashSet<String>();
		addresses.add("postal address");
		req.setAddresses(addresses);

		BindingResult result = Mockito.mock(BindingResult.class);
		Mockito.when(result.hasErrors()).thenReturn(false);

		ResponseEntity<UserResponse> user = userController.createUser(req, result);
		Assert.assertTrue(user.getBody().getFirstName().equals("FirstName"));

	}

	@Test(expected = UserException.class)
	public void testUserCreateThrowsException() throws UserException {
		Mockito.when(userService.createUser(Mockito.any()))
				.thenThrow(new UserException("Following emails already present : [email@email.com]"));

		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		req.setLastName("LastName");
		Set<String> emails = new HashSet<String>();
		emails.add("email@email.com");
		req.setEmails(emails);
		Set<String> addresses = new HashSet<String>();
		addresses.add("postal address");
		req.setAddresses(addresses);

		BindingResult result = Mockito.mock(BindingResult.class);
		Mockito.when(result.hasErrors()).thenReturn(false);

		try {
			userController.createUser(req, result);
		} catch (UserException e) {
			Assert.assertTrue(e.getMessage().equals("Following emails already present : [email@email.com]"));
			throw e;
		}

	}

}
