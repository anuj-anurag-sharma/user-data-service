package com.hootboard.userdata.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hootboard.userdata.entity.Client;

public interface ClientRepo extends PagingAndSortingRepository<Client, String>{

}
