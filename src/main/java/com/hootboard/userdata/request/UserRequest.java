package com.hootboard.userdata.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.hootboard.userdata.validation.EmailCollection;

import lombok.Data;

@Data
public class UserRequest {

	@NotNull
	private String firstName;

	private String lastName;

	@EmailCollection
	private Set<String> emails;

	private Set<String> addresses;
	
}
