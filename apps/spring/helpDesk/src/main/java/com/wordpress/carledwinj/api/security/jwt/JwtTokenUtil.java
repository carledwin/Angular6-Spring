package com.wordpress.carledwinj.api.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = 1932043537150345002L;

	private static String CLAIM_KEY_USERNAME = "sub";
	private static String CLAIM_KEY_CREATED = "created";
	private static String CLAIM_EXPIRED = "exp";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	//obtem o email dentro do token
	public String getUsernameFromToken(String token) {
		
		String username;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		}catch(Exception e) {
			username = null;
		}
		return username;
	}
	
	//retorna a data de expiracao do token
	public Date getExpirationDateFromToken(String token) {
		
		Date expiration;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		
		return expiration;
	}
	
	//faz o parse das informacoes do token jwt para extrair as informacoes contidade no token
	private Claims getClaimsFromToken(String token) {
		
		Claims claims;
		
		try {
			claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		
		return claims;
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims = new HashMap<>();
		
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		
		final Date createdDate = new Date();
		claims.put(CLAIM_KEY_CREATED, createdDate);
		
		return doGenerateToken(claims);
	}

	private String doGenerateToken(Map<String, Object> claims) {
		
		final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
		
		final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
		
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	public Boolean canBeTokenRefreshed(String token) {
		return (!isTokenExpired(token));
	}
	
	public String refreshToken(String token) {
		
		String refreshedToken;
		
		try {
			
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			
			refreshedToken = doGenerateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		
		return refreshedToken;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		JwtUser jwtUser = (JwtUser) userDetails;
		
		final String username = getUsernameFromToken(token);
		
		return(
				username.equals(jwtUser.getUsername())&&
				!isTokenExpired(token));
	}
}
