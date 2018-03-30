package com.hootboard.userdata.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Client {

	@Id
	private String id;

	private String name;

	private String hashPwd;

}
