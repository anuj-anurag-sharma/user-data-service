package com.hootboard.userdata.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hootboard.userdata.dao.CounterDao;
import com.hootboard.userdata.entity.Client;
import com.hootboard.userdata.exception.LoginFailureException;
import com.hootboard.userdata.exception.RegistrationException;
import com.hootboard.userdata.repo.ClientRepo;
import com.hootboard.userdata.request.LoginRequest;
import com.hootboard.userdata.request.RegistrationRequest;
import com.hootboard.userdata.service.ClientService;
import com.hootboard.userdata.service.util.PasswordUtil;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepo clientRepo;

	@Autowired
	private CounterDao counterDao;

	@Override
	public String registerClient(RegistrationRequest request) throws RegistrationException {
		Client client = new Client();
		client.setName(request.getName());
		client.setHashPwd(PasswordUtil.hashPassword(request.getPassword()));
		Long sequence = counterDao.getSequence("client");
		client.setId("CLIENT-" + sequence);
		Client save = clientRepo.save(client);
		return save.getId();
	}

	@Override
	public Boolean loginClient(LoginRequest login) throws LoginFailureException {
		Optional<Client> clientOpt = clientRepo.findById(login.getClientId());
		if (!clientOpt.isPresent()) {
			// Log this info -- "No client found with Id " + login.getClientId()
			throw new LoginFailureException("Client Id or Password Incorrect");
		}
		if (!clientOpt.get().getHashPwd().equals(PasswordUtil.hashPassword(login.getPwd()))) {
			throw new LoginFailureException("Client Id or Password Incorrect");
		}
		return Boolean.TRUE;
	}

}
