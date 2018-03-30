package com.hootboard.userdata.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.hootboard.userdata.dao.CounterDao;
import com.hootboard.userdata.entity.Counter;

@Repository
public class CounterDaoImpl implements CounterDao {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Long getSequence(String type) {
		Query query = new Query(Criteria.where("name").is(type));
		Update update = new Update().inc("sequence", 1);
		Counter counter = mongoTemplate.findAndModify(query, update, Counter.class);
		return counter.getSequence();
	}

}
