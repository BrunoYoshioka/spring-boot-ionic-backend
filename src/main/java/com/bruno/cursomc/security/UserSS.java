package com.bruno.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bruno.cursomc.dominio.enums.Perfil;
// classe de usuário conforme contrato do Spring Security 
// A classe implementa contrato do UserDetails
public class UserSS implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {
	}

	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis /*Recebendo uma lista de perfil*/ ) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities =  perfis.stream().map(x /*Para cada perfil x na lista de perfil */ 
				-> new SimpleGrantedAuthority(x.getDescricao() /*Pegar o string correspondente ao perfil*/ )).collect(Collectors.toList());
		// Converter conjunto Set para uma lista de GrantedAuthority
		// Para daca perfilV
	}
	
	public Integer getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()  {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	// A conta não está inspirada
	@Override
	public boolean isAccountNonExpired() {
		return true; // por padrão a conta não esta inspirada
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // por padrão a conta não está bloqueada
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // as credenciais não estão inspiradas
	}

	@Override
	public boolean isEnabled() {
		return true; // usuário está ativo
	}
	
	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}
}
