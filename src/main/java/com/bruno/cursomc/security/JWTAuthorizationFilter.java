package com.bruno.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//Criar um filtro de autorização 
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil; // classe auxiliar
	
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService; // precisamos usar porque o filtro terá que analizar o token para verificar se é válido, para isso precisamos extrair usuário, buscar do banco e verificar se existe
	}
	
	// Método intercepta a requisição e ver se realmente o usuário está autorizado
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain /*Objeto de cadeia do filtro do sistema */ ) throws IOException, ServletException{
		// Pegar valor Authorization do cabeçalho da requisição 
		String header = request.getHeader("Authorization"); // pegar esse valor e guardar numa variável do tipo string
		
		// Procedimento para liberar a autorização do usuário tentando acessar o endpoint
		// verificar se o header não tiver nulo e começa com "Bearer " se tudo for verdade instancio o objeto do Spring Security
		if(header != null && header.startsWith("Bearer ")){
			// mandar o valor que tiver na frente do Bearer e retornar objeto que tiver 
			UsernamePasswordAuthenticationToken auth = getAuthentication(request, header.substring(7)); // costruo objeto a partir do token 
			if(auth != null) /*Se o objeto construido for diferente de nulo (O token está tudo ok) */ {
				SecurityContextHolder.getContext().setAuthentication(auth); // chamo a função para liberar o acesso do meu filtro
			}
		}
		chain.doFilter(request, response); // Continuar a execução normal da requisição depois do teste de liberação do usuário
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
		if(jwtUtil.tokenValido(token)) /*Se o token for válido*/ {
			// Autorizado
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username); // instanciar 
		}
		return null;
	}
	
}
