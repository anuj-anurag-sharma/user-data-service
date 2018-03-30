package com.hootboard.userdata.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hootboard.userdata.entity.Client;

public interface ClientRepository extends PagingAndSortingRepository<Client, String> {


}
