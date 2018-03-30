package com.hootboard.userdata.response;

import lombok.Data;

@Data
public class AuthTokenResponse {

	private String token;

	private String generatedOn;

}
