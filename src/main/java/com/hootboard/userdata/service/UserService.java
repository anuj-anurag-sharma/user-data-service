package com.hootboard.userdata.service;

import java.util.List;

import com.hootboard.userdata.exception.NotFoundException;
import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.Response;

public interface UserService {

	Response createUser(UserRequest userRequest) throws UserException;

	Response updateUser(String id, UserRequest userRequest) throws UserException, NotFoundException;

	Response getUser(String id) throws NotFoundException;

	Response deleteUser(String id) throws NotFoundException;

	List<Response> getAllUsers() throws UserException;

}
