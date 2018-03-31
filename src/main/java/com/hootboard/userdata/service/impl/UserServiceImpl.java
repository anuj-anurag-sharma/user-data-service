package com.hootboard.userdata.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hootboard.userdata.dao.UserDao;
import com.hootboard.userdata.entity.User;
import com.hootboard.userdata.exception.NotFoundException;
import com.hootboard.userdata.exception.UserException;
import com.hootboard.userdata.repo.UserRepo;
import com.hootboard.userdata.request.UserRequest;
import com.hootboard.userdata.response.MessageResponse;
import com.hootboard.userdata.response.Response;
import com.hootboard.userdata.response.UserResponse;
import com.hootboard.userdata.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserDao userDao;

	@Override
	public UserResponse createUser(UserRequest userRequest) throws UserException {
		Set<String> emails = userRequest.getEmails();
		Set<String> matchingEmails = userDao.findEmailsAlreadyPresent(emails);
		if (matchingEmails.size() > 0) {
			LOGGER.error("createUser, Error : {}", "Following emails already present : " + matchingEmails.toString());
			throw new UserException("Following emails already present : " + matchingEmails.toString());
		}
		String clientId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User createRequest = transformToUser(userRequest);
		createRequest.setCreatedBy(clientId);
		createRequest.setCreatedOn(DateTime.now());
		User user = userRepo.save(createRequest);
		UserResponse ur = transformToUserResponse(user);
		return ur;
	}

	private UserResponse transformToUserResponse(User user) {
		UserResponse ur = new UserResponse();
		ur.setEmails(user.getEmails());
		ur.setId(user.getId());
		ur.setFirstName(user.getFirstName());
		ur.setLastName(user.getLastName());
		ur.setAddresses(user.getAddresses());
		return ur;
	}

	@Override
	public UserResponse updateUser(String id, UserRequest userRequest) throws UserException, NotFoundException {
		Optional<User> userOptional = userRepo.findById(id);
		if (!userOptional.isPresent()) {
			LOGGER.error("createUser, Error : {}", "No user with id: " + id + " is present");
			throw new NotFoundException("No user with id: " + id + " is present");
		}
		Set<String> additionalEmails = findAdditionalEmailsInRequest(userOptional.get().getEmails(),
				userRequest.getEmails());
		if (additionalEmails.size() > 0) {
			Set<String> alreadyPresent = userDao.findEmailsAlreadyPresent(additionalEmails);
			if (alreadyPresent.size() > 0) {
				LOGGER.error("createUser, Error : {}",
						"Following emails already present : " + alreadyPresent.toString());
				throw new UserException("Following emails already present : " + alreadyPresent.toString());
			}
		}
		String clientId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User updateRequest = transformToUser(userRequest);
		updateRequest.setId(id);
		updateRequest.setLastUpdatedBy(clientId);
		updateRequest.setLastUpdatedOn(DateTime.now());
		User user = userRepo.save(updateRequest);
		UserResponse ur = transformToUserResponse(user);
		return ur;
	}

	private Set<String> findAdditionalEmailsInRequest(Set<String> emailsInDb, Set<String> emailsInRequest) {
		if (emailsInDb != null && emailsInDb.size() > 0) {
			Set<String> set = emailsInRequest.stream().filter(e -> !emailsInDb.contains(e)).collect(Collectors.toSet());
			return set;
		}
		return emailsInRequest;
	}

	@Override
	public UserResponse getUser(String id) throws NotFoundException {
		Optional<User> userOptional = userRepo.findById(id);
		if (!userOptional.isPresent()) {
			LOGGER.error("createUser, Error : {}", "No user with id: " + id + " is present");
			throw new NotFoundException("No user with id: " + id + " is present");
		}
		User user = userOptional.get();
		UserResponse ur = transformToUserResponse(user);
		return ur;
	}

	@Override
	public MessageResponse deleteUser(String id) throws NotFoundException {
		Optional<User> userOptional = userRepo.findById(id);
		if (!userOptional.isPresent()) {
			LOGGER.error("createUser, Error : {}", "No user with id: " + id + " is present");
			throw new NotFoundException("No user with id: " + id + " is present");
		}
		User user = userOptional.get();
		user.setIsActive(Boolean.FALSE);
		String clientId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setLastUpdatedBy(clientId);
		user.setLastUpdatedOn(DateTime.now());
		userRepo.save(user);
		MessageResponse response = new MessageResponse();
		response.setMsg("User with Id " + id + " deleted successfully");
		return response;
	}

	@Override
	public List<Response> getAllUsers() throws UserException {
		try {
			List<User> users = (List<User>) userRepo.findAll();
			List<Response> list = users.stream().map(u -> transformToUserResponse(u)).collect(Collectors.toList());
			return list;
		} catch (Exception e) {
			throw new UserException(e);
		}
	}

	User transformToUser(UserRequest userRequest) {
		User user = new User();
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setEmails(userRequest.getEmails().stream().map(e -> e.toLowerCase()).collect(Collectors.toSet()));
		user.setAddresses(userRequest.getAddresses());
		user.setIsActive(true);
		return user;
	}

}
