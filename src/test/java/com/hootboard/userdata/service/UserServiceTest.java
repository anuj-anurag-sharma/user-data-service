package com.hootboard.userdata.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.hootboard.userdata.dao.UserDao;
import com.hootboard.userdata.entity.User;
import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.repo.UserRepo;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.UserResponse;
import com.hootboard.userdata.service.impl.UserServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
public class UserServiceTest {

	private UserService userSvc;

	@Mock
	private UserRepo userRepo;

	@Mock
	private UserDao userDao;

	@Mock
	SecurityContext context;

	@Mock
	Authentication auth;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		userSvc = new UserServiceImpl();
		ReflectionTestUtils.setField(userSvc, "userRepo", userRepo);
		ReflectionTestUtils.setField(userSvc, "userDao", userDao);
	}

	@Test
	public void testCreateUser() throws UserException {
		PowerMockito.mockStatic(SecurityContextHolder.class);
		Mockito.when(SecurityContextHolder.getContext()).thenReturn(context);
		Mockito.when(context.getAuthentication()).thenReturn(auth);
		Mockito.when(auth.getPrincipal()).thenReturn("test");
		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		req.setLastName("LastName");
		Set<String> emails = new HashSet<String>();
		emails.add("email@email.com");
		req.setEmails(emails);
		Set<String> addresses = new HashSet<String>();
		addresses.add("postal address");
		req.setAddresses(addresses);
		Mockito.when(userDao.findEmailsAlreadyPresent(Mockito.any())).thenReturn(Collections.emptySet());
		User user = new User();
		user.setAddresses(addresses);
		user.setCreatedBy("test");
		user.setCreatedOn(DateTime.now());
		user.setEmails(emails);
		user.setFirstName(req.getFirstName());
		user.setLastName(req.getLastName());
		user.setId("12345");
		Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);
		UserResponse response = userSvc.createUser(req);
		Assert.assertTrue("12345".equals(response.getId()));
	}

	@Test(expected = UserException.class)
	public void testCreateUserEmailsDuplicate() throws UserException {
		PowerMockito.mockStatic(SecurityContextHolder.class);
		Mockito.when(SecurityContextHolder.getContext()).thenReturn(context);
		Mockito.when(context.getAuthentication()).thenReturn(auth);
		Mockito.when(auth.getPrincipal()).thenReturn("test");
		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		req.setLastName("LastName");
		Set<String> emails = new HashSet<String>();
		emails.add("email@email.com");
		req.setEmails(emails);
		Set<String> addresses = new HashSet<String>();
		addresses.add("postal address");
		req.setAddresses(addresses);
		Mockito.when(userDao.findEmailsAlreadyPresent(Mockito.any())).thenReturn(emails);
		User user = new User();
		user.setAddresses(addresses);
		user.setCreatedBy("test");
		user.setCreatedOn(DateTime.now());
		user.setEmails(emails);
		user.setFirstName(req.getFirstName());
		user.setLastName(req.getLastName());
		user.setId("12345");
		Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);
		try {
			userSvc.createUser(req);
		} catch (UserException e) {
			Assert.assertTrue(e.getMessage().equals("Following emails already present : [email@email.com]"));
			throw e;
		}
	}

}
