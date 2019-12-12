package com.bruno.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Esta classe de configuração SecurityConfig para definir as configurações de segurança
// Esta classe deve herdar de WebSecurityConfigurerAdapter
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private Environment env;
	
	// vetor de Strings
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};
	
	// caminhos somente leitura (permitir apenas recuperação de dados)
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**",
			"/clientes/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// pegar profile ativo do projeto, se caso ele tiver no "test"
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			// irei acessar o H2
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			.antMatchers(PUBLIC_MATCHERS).permitAll() // passando o vetor de argumentos (todos os caminhos que tiver no PUBLIC_MATCHERS irá permitir)
			.anyRequest().authenticated(); // qualquer solicitação seja autenticada
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // segurar pra que o backend não irá criar excessão de usuários
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	    return source;
	}
	
	// Adicionando senha a Cliente - Incluir um Bean de BCryptPasswordEncoder no arquivo de configuração
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}