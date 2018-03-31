package com.hootboard.userdata.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class LoginRequest {

	@NotNull
	private String clientId;

	@NotNull
	private String password;

}
