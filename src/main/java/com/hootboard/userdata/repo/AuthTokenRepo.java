package com.hootboard.userdata.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hootboard.userdata.entity.AuthToken;

public interface AuthTokenRepo extends PagingAndSortingRepository<AuthToken, String>{

}
