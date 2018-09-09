package com.wordpress.carledwinj.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordpress.carledwinj.api.entity.User;
import com.wordpress.carledwinj.api.response.Response;
import com.wordpress.carledwinj.api.service.UserService;

@CrossOrigin(origins="")
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> create(HttpServletRequest request, @RequestBody User user, BindingResult bindingResult){
		
		Response<User> responseUser = new Response<User>();
		
		try {
			
			validateCreateUser(user, bindingResult);
			
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> responseUser.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(responseUser);
			}
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User userPersisted = userService.createOrUpdate(user);
			responseUser.setData(userPersisted);
			
			return ResponseEntity.ok(responseUser);
		}catch(DuplicateKeyException dE){
			
			responseUser.getErrors().add("Email already registered !");
			return ResponseEntity.badRequest().body(responseUser);
		} catch (Exception e) {
			
			responseUser.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseUser);
		}
		
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> update(HttpServletRequest httpServletRequest, @RequestBody User user, BindingResult bindingResult){
		
		Response<User> responseUser = new Response<User>();
		
		try {
			
			validateUpdateUser(user, bindingResult);
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> responseUser.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(responseUser);
			}
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User userUpdated = userService.createOrUpdate(user);
			
			responseUser.setData(userUpdated);
			
			return ResponseEntity.accepted().body(responseUser);
		} catch (Exception e) {
			
			responseUser.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseUser);
		}
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> findById(@PathVariable String id){
	
		Response<User> responseUser = new Response<User>();
		
		User user = userService.findById(id);
		
		if(user == null) {
			
			responseUser.getErrors().add("Register not fond id: " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseUser);
		}
		
		responseUser.setData(user);
		return ResponseEntity.ok(responseUser);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<?>> delete(@PathVariable String id){
	
		Response<User> responseUser = new Response<User>();
		
		User user = userService.findById(id);
		
		if(user == null) {
			
			responseUser.getErrors().add("Register not fond id: " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseUser);
		}
		
		userService.delete(user.getId());
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{page}/{count}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Response<Page<User>>> findAll(@PathVariable int page, @PathVariable int count){
		
		Response<Page<User>> responsePageUsers = new Response<Page<User>>();
		
		Page<User> pageUsers = userService.findAll(page, count);
		
		responsePageUsers.setData(pageUsers);
		return ResponseEntity.ok(responsePageUsers);
	}
	
	
	private void validateCreateUser(User user, BindingResult bindingResult) {
		
		if(user.getEmail() == null) {
			bindingResult.addError(new ObjectError("User", "Email no information!"));
		}
	}
	
	private void validateUpdateUser(User user, BindingResult bindingResult) {
		
		if(user.getId() == null) {
			bindingResult.addError(new ObjectError("User", "Id no information!"));
		}
		
		if(user.getEmail() == null) {
			bindingResult.addError(new ObjectError("User", "Email no information!"));
		}
	}
	
}
