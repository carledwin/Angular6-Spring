package com.wordpress.carledwinj.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wordpress.carledwinj.api.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);
}
