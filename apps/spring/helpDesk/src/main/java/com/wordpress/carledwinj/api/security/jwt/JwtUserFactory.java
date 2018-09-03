package com.wordpress.carledwinj.api.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.wordpress.carledwinj.api.entity.User;
import com.wordpress.carledwinj.api.enums.ProfileEnum;

/**
 * Converter o user do banco em userDetails do Spring Security
 * @author carledwin
 *
 */
public class JwtUserFactory {

	private JwtUserFactory() {
	}
	
	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getEmail(), user.getPassword(), mapToGrantedAuthorities(user.getProfile()));
	}

	private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		
		return authorities ;
	}
}
