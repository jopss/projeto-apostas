package com.jopss.apostas.servicos.security;

import com.jopss.apostas.modelos.Permissao;
import com.jopss.apostas.modelos.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Classe encapsuladora do 'UserDetails', que conterá o 'Usuario' logado.
 */
public class UserDetailsWrapper implements UserDetails{
	
	private final Usuario usuario;
	private List<GrantedAuthority> authorities = new ArrayList<>();

	public UserDetailsWrapper(Usuario usuario) {
		this.usuario = usuario;
		this.authorities = new ArrayList<>();
                
                for(Permissao permissao : usuario.getPerfil().getPermissoes()){
                        this.authorities.add(new SimpleGrantedAuthority(permissao.getPapel().name()));
                }
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

        public Usuario getUsuario() {
                return usuario;
        }

}
