package com.bruno.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component // pois ele pode ser injetado a outras classes
public class JWTUtil {
	
	// gerar o token e precisamos de duas propriedades
	@Value("${jwt.secret}") // pegar essa propriedade do application
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username) /*setSubject é Usuário*/
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// assinar o token (algoritmo e o segredo)
				.signWith(SignatureAlgorithm.HS512, secret.getBytes() /*Método de ArrayBytes*/)
				.compact();
	}
}