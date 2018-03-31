package com.hootboard.userdata.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hootboard.userdata.exception.GlobalExceptionHandler;
import com.hootboard.userdata.exception.NotFoundException;
import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.MessageResponse;
import com.hootboard.userdata.response.Response;
import com.hootboard.userdata.response.UserResponse;
import com.hootboard.userdata.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserApiTest {

	@InjectMocks
	private UserController controller;

	private MockMvc mockMvc;

	@Mock
	UserService userSvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		final StaticApplicationContext applicationContext = new StaticApplicationContext();
		applicationContext.registerSingleton("exceptionHandler", GlobalExceptionHandler.class);

		final WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();
		webMvcConfigurationSupport.setApplicationContext(applicationContext);

		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setHandlerExceptionResolvers(webMvcConfigurationSupport.handlerExceptionResolver()).build();
	}

	@Test
	public void createUser() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");
		Mockito.when(userSvc.createUser(Mockito.any())).thenReturn(resp);
		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/secure/user", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "{\"id\":\"12345\",\"firstName\":\"FirstName\",\"lastName\":null,\"emails\":null,\"addresses\":null}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void createUserNoFirstName() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		Mockito.when(userSvc.createUser(Mockito.any())).thenReturn(resp);
		UserRequest req = new UserRequest();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/secure/user", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"[@firstName:must not be null]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void createUserInvalidEmail() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		Mockito.when(userSvc.createUser(Mockito.any())).thenReturn(resp);
		UserRequest req = new UserRequest();
		req.setFirstName("firstName");
		Set<String> emails = new HashSet<>();
		emails.add("invalid-email");
		req.setEmails(emails);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/secure/user", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"[@emails:Invalid Email]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void createUserHandleNull() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");
		Mockito.when(userSvc.createUser(Mockito.any())).thenReturn(resp);
		UserRequest req = null;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/secure/user", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
	}

	@Test
	public void createUserThrowsException() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");
		Mockito.when(userSvc.createUser(Mockito.any()))
				.thenThrow(new UserException("Following emails already present : [email@email.com]"));
		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/secure/user", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"Following emails already present : [email@email.com]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void getUser() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");
		Mockito.when(userSvc.getUser(Mockito.any())).thenReturn(resp);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/secure/user/12345")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "{\"id\":\"12345\",\"firstName\":\"FirstName\",\"lastName\":null,\"emails\":null,\"addresses\":null}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void getUserHandleNull() throws Exception {

		Mockito.when(userSvc.getUser(Mockito.any())).thenThrow(new NotFoundException("No user with id: null is present"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/secure/user/" + null)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();

		String expected = "{\"error\":\"No user with id: null is present\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void getUserThrowsException() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");
		Mockito.when(userSvc.getUser(Mockito.any())).thenThrow(new NotFoundException("No user with id: 12345 is present"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/secure/user/12345")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();

		String expected = "{\"error\":\"No user with id: 12345 is present\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void getAllUsers() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");

		UserResponse resp1 = new UserResponse();
		resp1.setId("12346");
		resp1.setFirstName("Test");
		List<Response> list = new ArrayList<>();
		list.add(resp);
		list.add(resp1);
		Mockito.when(userSvc.getAllUsers()).thenReturn(list);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/secure/user")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "[{\"id\":\"12345\",\"firstName\":\"FirstName\",\"lastName\":null,\"emails\":null,\"addresses\":null}"
				+ ",{\"id\":\"12346\",\"firstName\":\"Test\",\"lastName\":null,\"emails\":null,\"addresses\":null}]";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void getAllUsersReturnsEmpty() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");
		Mockito.when(userSvc.getAllUsers()).thenReturn(Collections.emptyList());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/secure/user")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "[]";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void updateUser() throws Exception {

		UserResponse resp = new UserResponse();
		resp.setId("12345");
		resp.setFirstName("FirstName");
		Mockito.when(userSvc.updateUser(Mockito.anyString(), Mockito.any())).thenReturn(resp);
		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/secure/user/12345")
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "{\"id\":\"12345\",\"firstName\":\"FirstName\",\"lastName\":null,\"emails\":null,\"addresses\":null}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void updateUserThrowsEmailAlreadyPresentError() throws Exception {

		Mockito.when(userSvc.updateUser(Mockito.anyString(), Mockito.any()))
				.thenThrow(new UserException("Following emails already present : [email@email.com]"));
		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/secure/user/12345")
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"Following emails already present : [email@email.com]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void updateUserThrowsUserNotPresentError() throws Exception {

		Mockito.when(userSvc.updateUser(Mockito.anyString(), Mockito.any()))
				.thenThrow(new NotFoundException("No user with id: 12345 is present"));
		UserRequest req = new UserRequest();
		req.setFirstName("FirstName");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/secure/user/12345")
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();

		String expected = "{\"error\":\"No user with id: 12345 is present\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void deleteUser() throws Exception {

		MessageResponse value = new MessageResponse();
		value.setMsg("User with id 12345 deleted successfully");
		Mockito.when(userSvc.deleteUser(Mockito.anyString())).thenReturn(value);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/secure/user/12345")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "{\"msg\":\"User with id 12345 deleted successfully\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void deleteUserThrowsUserNotPresentError() throws Exception {

		Mockito.when(userSvc.deleteUser(Mockito.anyString()))
				.thenThrow(new NotFoundException("No user with id: 12345 is present"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/secure/user/12345")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();

		String expected = "{\"error\":\"No user with id: 12345 is present\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

}
