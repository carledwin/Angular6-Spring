package com.wordpress.carledwinj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wordpress.carledwinj.entity.Student;
import com.wordpress.carledwinj.repository.StudentRepository;

@Service
public class StudentService {

	private StudentRepository studentRepository;
	
	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
	
	public List<Student> findAll() {
		return this.studentRepository.findAll();
	}

	public Student save(Student student) {
		return studentRepository.save(student);
	}

	public Student findOne(String id) {
		return this.studentRepository.findOne(id);
	}

	public List<Student> findByNameLikeIgnoreCase(String name) {
		return this.studentRepository.findByNameLikeIgnoreCase(name);
	}

}
