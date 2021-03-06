package com.wordpress.carledwinj.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wordpress.carledwinj.api.entity.User;
import com.wordpress.carledwinj.api.security.jwt.JwtUserFactory;
import com.wordpress.carledwinj.api.service.UserService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userService.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'", username));
		}else {
			return JwtUserFactory.create(user);
		}
	}
}
