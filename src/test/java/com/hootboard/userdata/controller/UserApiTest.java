package com.hootboard.userdata.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.request.UserRequest;
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

}
