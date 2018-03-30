package com.hootboard.userdata.response;

import java.util.Set;

import lombok.Data;

@Data
public class UserResponse {

	private String id;

	private String firstName;

	private String lastName;

	private Set<String> emails;

	private Set<String> addresses;

}
