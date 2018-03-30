package com.hootboard.userdata.response;

import lombok.Data;

@Data
public class ErrorResponse {

	private String error;

	public ErrorResponse(String error) {
		this.error = error;
	}

	public ErrorResponse() {

	}

}
