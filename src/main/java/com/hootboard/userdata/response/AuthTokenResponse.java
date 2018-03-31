package com.hootboard.userdata.response;

import lombok.Data;

@Data
public class AuthTokenResponse extends Response{

	private String token;

	private String generatedOn;

}
