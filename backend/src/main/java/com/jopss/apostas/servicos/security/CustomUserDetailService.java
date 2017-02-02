package com.jopss.apostas.servicos.security;

import com.jopss.apostas.modelos.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Indica ao security que o UserPrincipal nao eh o interno, mas sim, o nosso
 * 'UserDetailsWrapper' que contera o 'Usuario' logado.
 */
public class CustomUserDetailService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = new Usuario(login).buscarPorLogin();
		if (usuario == null) {
			throw new UsernameNotFoundException(login);
		}
		return new UserDetailsWrapper(usuario);
	}
	
}
