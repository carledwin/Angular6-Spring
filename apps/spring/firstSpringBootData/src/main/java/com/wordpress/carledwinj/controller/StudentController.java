package com.wordpress.carledwinj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wordpress.carledwinj.entity.Student;
import com.wordpress.carledwinj.service.StudentService;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;

	@GetMapping("/student")
	public List<Student> findAll(){
		return this.studentService.findAll();
	}
	
	@GetMapping("/student/{id}")
	public Student findOne(@PathVariable String id){
		return this.studentService.findOne(id);
	}
	
	@GetMapping("/student/{name}/name")
	public List<Student> findByNameLikeIgnoreCase(@PathVariable String name){
		return this.studentService.findByNameLikeIgnoreCase(name);
	}
	
	@PostMapping("/student")
	public Student saveStudent(@RequestBody Student student) {
		return this.studentService.save(student);
	}
}
