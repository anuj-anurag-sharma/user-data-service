package com.hootboard.userdata.dao;

import java.util.Set;

public interface UserDao {

	Set<String> findEmailsAlreadyPresent(Set<String> emails);

}
