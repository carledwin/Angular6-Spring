package com.wordpress.carledwinj.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wordpress.carledwinj.entity.Student;

public interface StudentRepository extends MongoRepository<Student, String>{

	List<Student> findByNameLikeIgnoreCase(String name);

}
