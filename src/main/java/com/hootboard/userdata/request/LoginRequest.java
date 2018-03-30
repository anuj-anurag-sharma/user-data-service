package com.hootboard.userdata.request;

import lombok.Data;

@Data
public class LoginRequest {

	private String clientId;
	
	private String pwd;
	
}
