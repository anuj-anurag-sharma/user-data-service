package com.hootboard.userdata.entity;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class AuthToken {

	@Id
	private String authToken;

	private String clientId;

	private DateTime generatedOn;

	public AuthToken() {

	}

	public AuthToken(String clientId, DateTime now) {
		this.clientId = clientId;
		this.generatedOn = now;
	}

}
