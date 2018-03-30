package com.hootboard.userdata.service;

import java.util.List;

import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.UserResponse;

public interface UserService {

	UserResponse createUser(UserRequest userRequest) throws UserException;

	UserResponse updateUser(String id, UserRequest userRequest) throws UserException;

	UserResponse getUser(String id) throws UserException;

	Boolean deleteUser(String id) throws UserException;

	List<UserResponse> getAllUsers();

}
