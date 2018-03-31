package com.hootboard.userdata.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hootboard.userdata.dao.CounterDao;
import com.hootboard.userdata.entity.Client;
import com.hootboard.userdata.exception.LoginException;
import com.hootboard.userdata.exception.RegistrationException;
import com.hootboard.userdata.repo.ClientRepo;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.request.RegistrationRequest;
import com.hootboard.userdata.response.MessageResponse;
import com.hootboard.userdata.response.Response;
import com.hootboard.userdata.service.ClientService;
import com.hootboard.userdata.service.util.PasswordUtil;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepo clientRepo;

	@Autowired
	private CounterDao counterDao;

	@Override
	public Response registerClient(RegistrationRequest request) throws RegistrationException {
		Client client = new Client();
		client.setName(request.getName());
		client.setHashPwd(PasswordUtil.hashPassword(request.getPassword()));
		Long sequence = counterDao.getSequence("client");
		client.setId("client-" + sequence);
		Client save = clientRepo.save(client);
		MessageResponse response = new MessageResponse();
		response.setMsg(save.getId());
		return response;
	}

	@Override
	public void loginClient(LoginRequest login) throws LoginException {
		Optional<Client> clientOpt = clientRepo.findById(login.getClientId().toLowerCase());
		if (!clientOpt.isPresent()) {
			throw new LoginException("Client Id or Password Incorrect");
		}
		if (!clientOpt.get().getHashPwd().equals(PasswordUtil.hashPassword(login.getPassword()))) {
			throw new LoginException("Client Id or Password Incorrect");
		}
	}

}
