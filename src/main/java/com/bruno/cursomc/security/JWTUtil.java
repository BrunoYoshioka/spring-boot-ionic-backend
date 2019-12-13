package com.bruno.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
	
	// método boolean para testar se o token é valido
	public boolean tokenValido(String token) {
		// claims ele é do tipo JWT que armazena as reindivicações (claims) do token 
		Claims claims = getClaims(token);
		if(claims != null) /*Quer dizer que ele pegou os claims do token */ {
			String username = claims.getSubject(); // obter username a partir dos claims que é função para retornar o usuário
			Date expirationDate = claims.getExpiration(); // pegar a data de inspiração a partir dos clientes
			Date now = new Date(System.currentTimeMillis()); // para testar se o token está expirado
			if(username != null && expirationDate != null && now.before(expirationDate)) /* se ele tiver usuário e também a data do token e a data for anterior a data de expiração (token válido)*/{
				return true;
			}
		}
		return false; // se algumas das acima falhar
	}
	
	// método pegar usuário a partir do token
	public String getUsername(String token) {
		// claims ele é do tipo JWT que armazena as reindivicações (claims) do token 
		Claims claims = getClaims(token);
		if(claims != null) /*Quer dizer que ele pegou os claims do token */ {
			return claims.getSubject(); 
		}
		return null;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody(); // função que recupera os claims a partir de um token, como é inválido pode gerar excessão por isso usei o try
		} catch (Exception e) {
			return null;
		}
	}
}