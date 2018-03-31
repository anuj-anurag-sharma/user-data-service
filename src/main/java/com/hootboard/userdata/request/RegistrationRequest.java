package com.hootboard.userdata.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class RegistrationRequest {

	@NotNull
	private String name;

	@NotNull
	private String password;

}
