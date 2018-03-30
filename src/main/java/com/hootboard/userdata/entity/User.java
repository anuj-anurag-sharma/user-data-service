package com.hootboard.userdata.entity;

import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class User {

	@Id
	private String id;

	private String firstName;

	private String lastName;

	private Set<String> emails;

	private Set<String> addresses;

	private DateTime createdOn;

	private String createdBy;

	private DateTime lastUpdatedOn;

	private String lastUpdatedBy;

	private Boolean isActive;

}
