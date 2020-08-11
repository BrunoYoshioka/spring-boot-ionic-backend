package com.bruno.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bruno.cursomc.dto.CredenciaisDTO;

// para que seja o filtro de autenticação será extendido UsernamePasswordAuthenticationFilter
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	
	private JWTUtil jwtUtil;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    	setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler()); // chamar a classe com a instancia para corrigir o problema no spring boot 2
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
	
	// o SpringSecurity sabe que esse filtro UsernamePasswordAuthenticationFilter vai ter que interceptar a requisição de login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
											HttpServletResponse res) throws AuthenticationException {
		// método autenticar
		try {
			CredenciaisDTO creds = new ObjectMapper() /*Instanciando objeto do CredenciaisDTO apartir dos dados que vieram na requisição*/
					.readValue(req.getInputStream()/*Operação que irá tentar pegar objeto de requisição (req) que vieram da requisição*/, CredenciaisDTO.class /*Converter */);
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
			
			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	// Caso a autenticação ocorrer com sucesso
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		res.addHeader("Authorization", "Bearer " + token);
		res.addHeader("access-control-expose-headers", "Authorization"); // expondo o cabeçalho Authorization
	}
	
	// Para o Spring Boot versão 2.0 é preciso acrescentar  uma implementação de AuthenticationFailureHandler e injetá-la em JWTAuthenticationFilter
	// Para poder personalizar o que acontecerá caso autenticação falhe
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		 
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401); // colocar na mão que tem que retronar 401
            response.setContentType("application/json"); // retornar corpo json
            response.getWriter().append(json());
        }
        
        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
        }
    }
}
