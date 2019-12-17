package com.bruno.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.cursomc.security.JWTUtil;
import com.bruno.cursomc.security.UserSS;
import com.bruno.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResources {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST) // protegido pela autenticação, o usuário deverá estar logado
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated(); // pegar usuário logado
		String token = jwtUtil.generateToken(user.getUsername()); // gerar novo token com o meu usuário e será gerado o token com a data atual, o tempo de espiração será renovado
		response.addHeader("Authorization", "Bearer " + token); // adiciono token na resposta na minha requisição
		return ResponseEntity.noContent().build();
	}
}