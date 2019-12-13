package com.bruno.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.Cliente;
import com.bruno.cursomc.repositories.ClienteRepository;
import com.bruno.cursomc.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private ClienteRepository repo;

	// busca pelo usuário pelo email
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cli = repo.findByEmail(email); // buscar cliente por email
		if(cli == null) /*Se esse cliente que busquei for nulo*/ {
			throw new UsernameNotFoundException(email);
		}	
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}
}