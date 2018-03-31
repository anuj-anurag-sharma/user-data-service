package com.hootboard.userdata.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.joda.time.DateTime;
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
import com.hootboard.userdata.exception.LoginException;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.request.RegistrationRequest;
import com.hootboard.userdata.response.AuthTokenResponse;
import com.hootboard.userdata.response.MessageResponse;
import com.hootboard.userdata.service.AuthService;
import com.hootboard.userdata.service.ClientService;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClientApiTest {

	@InjectMocks
	private ClientController controller;

	private MockMvc mockMvc;

	@Mock
	ClientService clientSvc;

	@Mock
	AuthService authSvc;

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
	public void registerClient() throws Exception {

		MessageResponse resp = new MessageResponse();
		resp.setMsg("CLIENT-1");
		Mockito.when(clientSvc.registerClient(Mockito.any())).thenReturn(resp);
		RegistrationRequest req = new RegistrationRequest();
		req.setName("client");
		req.setPassword("password");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/register", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "{\"msg\":\"CLIENT-1\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void registerClientWithoutNameError() throws Exception {

		MessageResponse resp = new MessageResponse();
		resp.setMsg("CLIENT-1");
		Mockito.when(clientSvc.registerClient(Mockito.any())).thenReturn(resp);
		RegistrationRequest req = new RegistrationRequest();
		req.setPassword("password");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/register", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"[@name:must not be null]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void registerClientWithoutPasswordError() throws Exception {

		MessageResponse resp = new MessageResponse();
		resp.setMsg("CLIENT-1");
		Mockito.when(clientSvc.registerClient(Mockito.any())).thenReturn(resp);
		RegistrationRequest req = new RegistrationRequest();
		req.setName("client");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/register", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"[@password:must not be null]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void loginClientSuccessful() throws Exception {
		Mockito.doNothing().when(clientSvc).loginClient(Mockito.any());
		AuthTokenResponse response = new AuthTokenResponse();
		String string = DateTime.now().toString();
		response.setGeneratedOn(string);
		response.setToken("12345");
		Mockito.when(authSvc.getAuthToken(Mockito.any())).thenReturn(response);

		LoginRequest req = new LoginRequest();
		req.setClientId("CLIENT-1");
		req.setPassword("password");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/auth-token", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expected = "{\"token\":\"12345\",\"generatedOn\":\"" + string + "\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);

	}

	@Test
	public void loginClientUnsuccessful() throws Exception {
		Mockito.doThrow(new LoginException("Invalid Id and password")).when(clientSvc).loginClient(Mockito.any());
		AuthTokenResponse response = new AuthTokenResponse();
		String string = DateTime.now().toString();
		response.setGeneratedOn(string);
		response.setToken("12345");
		Mockito.when(authSvc.getAuthToken(Mockito.any())).thenReturn(response);

		LoginRequest req = new LoginRequest();
		req.setClientId("CLIENT-1");
		req.setPassword("password");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/auth-token", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized()).andReturn();

		String expected = "{\"error\":\"Invalid Id and password\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);

	}

	@Test
	public void loginClientWithoutIdError() throws Exception {
		AuthTokenResponse response = new AuthTokenResponse();
		String string = DateTime.now().toString();
		response.setGeneratedOn(string);
		response.setToken("12345");
		Mockito.when(authSvc.getAuthToken(Mockito.any())).thenReturn(response);

		LoginRequest req = new LoginRequest();
		req.setPassword("password");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/auth-token", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"[@clientId:must not be null]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);

	}

	@Test
	public void loginClientWithoutPasswordError() throws Exception {
		AuthTokenResponse response = new AuthTokenResponse();
		String string = DateTime.now().toString();
		response.setGeneratedOn(string);
		response.setToken("12345");
		Mockito.when(authSvc.getAuthToken(Mockito.any())).thenReturn(response);

		LoginRequest req = new LoginRequest();
		req.setClientId("CLIENT-1");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/auth-token", req)
				.content(new ObjectMapper().writeValueAsString(req)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();

		String expected = "{\"error\":\"[@password:must not be null]\"}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);

	}

}
