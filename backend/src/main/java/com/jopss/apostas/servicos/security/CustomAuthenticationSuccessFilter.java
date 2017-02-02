package com.jopss.apostas.servicos.security;

import com.jopss.apostas.modelos.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe filtro chamada apos cada login com sucesso.
 */
public class CustomAuthenticationSuccessFilter extends SavedRequestAwareAuthenticationSuccessHandler {

        @Override
        @Transactional
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
                UserDetailsWrapper details = (UserDetailsWrapper) authResult.getPrincipal();
                Usuario usuario = details.getUsuario();
                //use o usuario como quiser...
                
                super.onAuthenticationSuccess(request, response, authResult);
        }

}
