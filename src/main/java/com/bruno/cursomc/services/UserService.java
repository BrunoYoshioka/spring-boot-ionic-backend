package com.bruno.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.bruno.cursomc.security.UserSS;
// UserService com um método que retorna o usuário logado
public class UserService {
	// método estático pq ela vai ser chamado independente de instanciar o UserServive
	// retornar UserSS
	public static UserSS authenticated() {
		try {
			// retorna o usuário logado no sistema
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch(Exception e) {
			return null;
		}
	}
}
