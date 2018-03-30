package com.hootboard.userdata.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hootboard.userdata.entity.User;

public interface UserRepo extends PagingAndSortingRepository<User, String> {

	@Query("{'id': ?0, 'isActive':true }")
	Optional<User> findById(String id);

	@Query("{'isActive':true}")
	Iterable<User> findAll();
	
	

}
