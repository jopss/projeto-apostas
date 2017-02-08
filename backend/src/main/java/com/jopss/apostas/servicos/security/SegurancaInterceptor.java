package com.jopss.apostas.servicos.security;

import com.jopss.apostas.excecoes.TokenExpiradoException;
import com.jopss.apostas.excecoes.TokenInvalidoException;
import com.jopss.apostas.modelos.SegurancaAPI;
import com.jopss.apostas.modelos.enums.RoleEnum;
import com.jopss.apostas.servicos.security.annotation.Privado;
import com.jopss.apostas.servicos.security.annotation.Publico;
import com.jopss.apostas.web.form.RetornoLoginForm;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.oltu.oauth2.common.error.OAuthError.CodeResponse;
import org.apache.oltu.oauth2.common.error.OAuthError.ResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SegurancaInterceptor extends HandlerInterceptorAdapter {

        @Autowired
        private SegurancaServico segurancaServico;
        
        @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String metodoHTTP = request.getMethod();
		if (metodoHTTP.equalsIgnoreCase("OPTIONS")) {
			return true;
		}
		
                if(handler instanceof HandlerMethod){
                        HandlerMethod met = (HandlerMethod) handler;
                        
                        //acesso publico, libera
                        Publico annotPublico = met.getMethodAnnotation(Publico.class);
                        if(annotPublico!=null){
                                return true;
                        }
                        
                        RetornoLoginForm oauthResp = null;
                        try {
                                segurancaServico.verificaValidadeTokenAdicionandoNoContexto(request);
                        } catch (TokenInvalidoException e) {
                                oauthResp = segurancaServico.retornarErroOAuth(HttpServletResponse.SC_FORBIDDEN, ResourceResponse.INVALID_TOKEN, e, response);
                        } catch (TokenExpiradoException e) {
                                oauthResp = segurancaServico.retornarErroOAuth(HttpServletResponse.SC_FORBIDDEN, ResourceResponse.EXPIRED_TOKEN, e, response);
                        } catch (Exception e) {
                                e.printStackTrace();
                                oauthResp = segurancaServico.retornarErroOAuth(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, CodeResponse.SERVER_ERROR, e, response);
                        }
                        if (oauthResp != null) {
                                return false;
                        }
                                
                        //acesso privado, verifica as roles do usuario com a role configurada no metodo.
                        Privado annotPrivado = met.getMethodAnnotation(Privado.class);
                        if(annotPrivado!=null){
                                RoleEnum roleConfigurada = annotPrivado.role();
                                SegurancaAPI usuarioLogado = segurancaServico.getUsuarioLogado();
                                if( !usuarioLogado.getUsuario().getPerfil().contemRoleOuAdmin(roleConfigurada) ){
                                        segurancaServico.retornarErroOAuth(HttpServletResponse.SC_UNAUTHORIZED, "Sem permissao para abrir esta pagina", null, response);
                                        return false;
                                }
                        }
                }
		return true;
	}
        
}