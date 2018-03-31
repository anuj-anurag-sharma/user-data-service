package com.hootboard.userdata.dao.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.hootboard.userdata.dao.UserDao;
import com.hootboard.userdata.entity.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private MongoOperations mongo;

	@Override
	public Set<String> findEmailsAlreadyPresent(Set<String> emails) {
		if (emails == null || emails.isEmpty()) {
			return Collections.emptySet();
		}
		Set<String> emailsInLC = emails.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet());
		List<Criteria> criterias = emailsInLC.stream().map(e -> where("emails").is(e.toLowerCase()))
				.collect(Collectors.toList());

		Criteria consolidated = new Criteria().orOperator(criterias.toArray(new Criteria[0])).and("isActive").is(true);
		List<User> list = mongo.find(query(consolidated), User.class);
		Set<String> allEmails = list.stream().flatMap(u -> u.getEmails().stream().filter(a -> emailsInLC.contains(a)))
				.collect(Collectors.toSet());
		return allEmails;
	}

}
