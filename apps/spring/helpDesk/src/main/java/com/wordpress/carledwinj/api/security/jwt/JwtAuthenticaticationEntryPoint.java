package com.wordpress.carledwinj.api.security.jwt;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Trata erros de autenticacao
 * @author carledwin
 *
 */
@Component
public class JwtAuthenticaticationEntryPoint implements AuthenticationEntryPoint, Serializable{

	private static final long serialVersionUID = 1L;

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException authException) throws IOException, ServletException {
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Falha usuário não autorizado. - Unauthorized");
	}

}
