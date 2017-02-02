package com.jopss.apostas.servicos.security;

import com.jopss.apostas.modelos.Usuario;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.security.core.GrantedAuthority;

/**
 * Classe utilitaria para ser utilizado nos controladores a fim de retornar
 * usuario logado.
 */
@Component
public class SessionUserSupport {

	public boolean verifyIfLoggedUserHasPermissions(String... permissoes) {
		SecurityContext sx = SecurityContextHolder.getContext();
		if (sx != null) {
			Authentication auth = sx.getAuthentication();
			if (auth != null) {
				for (String permissao : permissoes) {
					boolean hasPermission = false;
					for (GrantedAuthority authority : auth.getAuthorities()) {
						if (authority.getAuthority().equals(permissao)) {
							hasPermission = true;
							break;
						}
					}

					if (!hasPermission) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
        
        public Usuario getUsuarioLogado(){
                UserDetailsWrapper usu = this.getUserDetailsWrapper();
                if(usu!=null){
                        return usu.getUsuario();
                }
                return null;
        }

	private UserDetailsWrapper getUserDetailsWrapper() {
		SecurityContext sx = SecurityContextHolder.getContext();
		if (sx != null) {
			Authentication auth = sx.getAuthentication();
			if (auth != null) {
                                if(auth.getPrincipal() instanceof UserDetailsWrapper){
                                        return (UserDetailsWrapper) auth.getPrincipal();
                                }
			}
		}
		return null;
	}

	/**
	 * Atualiza a atual sessão do Spring Security com um novo
	 * {@link UserDetailsWrapper} baseado no que foi passado.
	 *
	 * Atenção: A nova sessão é forçadamente autenticada e não valida
	 * credenciais. Uma acesso indevido gerará uma autenticação indesejada.
	 *
	 * TODO: deve ser verificado se a sessão anterior estava autenticada?
	 *
	 * @param details {@link UserDetailsWrapper}
	 */
	private void refreshAuthenticatedSessionWithNewDetails(UserDetailsWrapper details) {
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
		newAuth.setDetails(details);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	public void forceAuthenticateProfile(Usuario usuario) {
		this.refreshAuthenticatedSessionWithNewDetails( new UserDetailsWrapper(usuario) );
	}
}
