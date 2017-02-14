package com.jopss.apostas.servicos.security;

import com.jopss.apostas.excecoes.TokenCriadoException;
import com.jopss.apostas.excecoes.TokenExpiradoException;
import com.jopss.apostas.excecoes.TokenInvalidoException;
import com.jopss.apostas.excecoes.UsuarioOuSenhaInvalidaException;
import com.jopss.apostas.modelos.SegurancaAPI;
import com.jopss.apostas.modelos.Usuario;
import com.jopss.apostas.servicos.repositorio.SegurancaRepository;
import com.jopss.apostas.servicos.repositorio.UsuarioRepository;
import com.jopss.apostas.util.DateUtilsApostas;
import com.jopss.apostas.util.FormatadorUtil;
import com.jopss.apostas.web.form.RetornoLoginForm;
import com.jopss.apostas.web.util.OptionsFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.error.OAuthError.CodeResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contexto de seguranca.
 */
@Service
public class SegurancaServico {

        private final String APP_CLIENT_ID = "exemploaplicativocliente";
        private final String APP_CLIENT_PASSWD = "9834ba657bb2c60b5bb53de6f4201905";

        @Autowired
        private SegurancaRepository segurancaRepository;

        @Autowired
        private UsuarioRepository usuarioRepository;

        private SegurancaAPI retornarPorToken(String token) {
                return this.segurancaRepository.findByToken(token);
        }

        private SegurancaAPI retornarPorUsuario(Usuario usuario) {
                return this.segurancaRepository.findByUsuario(usuario);
        }

        /**
         * Metodo usado para verificar a validade do Token da
         * requisicao. Invocado no interceptor antes do
         * acesso ao recurso.
         *
         * @param request
         * @throws TokenExpiradoException
         * @throws TokenInvalidoException
         */
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public synchronized void verificaValidadeTokenAdicionandoNoContexto(HttpServletRequest request) throws TokenExpiradoException, TokenInvalidoException {
                try {
                        OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
                        String token = oauthRequest.getAccessToken();

                        if (StringUtils.isBlank(token)) {
                                throw new TokenInvalidoException("Token vazio.");
                        }

                        SegurancaAPI segurancaAPI = this.retornarPorToken(token);
                        if (segurancaAPI == null) {
                                throw new TokenInvalidoException("Token invalido.");
                        }

                        Usuario usuario = segurancaAPI.getUsuario();
                        if (usuario == null) {
                                throw new TokenInvalidoException("Problema interno no retorno do usuario: nulo.");
                        }

                        if (segurancaAPI.getToken().contains(token)) {
                                if (segurancaAPI.expirado()) {
                                        segurancaAPI.expirarToken();
                                        this.segurancaRepository.save(segurancaAPI);
                                        throw new TokenExpiradoException("Token de acesso expirado. Gere um novo token e tente novamente.");
                                } else {
                                        Hibernate.initialize(segurancaAPI.getUsuario().getPerfil().getPermissoes()); //force! :/
                                        SegurancaAPIThreadLocal.setSegurancaAPI(segurancaAPI);
                                }
                        } else {
                                throw new TokenInvalidoException("Token invalido. Tente novamente.");
                        }
                } catch (OAuthProblemException e) {
                        throw new TokenInvalidoException("Login invalido. Tente novamente.");
                } catch (OAuthSystemException ex) {
                        throw new RuntimeException(ex);
                }
        }

        /**
         * Metodo chamado pelo WS para guardar o Token temporario da empresa a
         * ser retornado para o cliente da requisicao.
         *
         * @param chave
         * @param token
         */
        private synchronized void atualizarToken(Usuario usuario, String token, Date proximaDataExpiracao) throws TokenInvalidoException, TokenExpiradoException, TokenCriadoException {
                if (usuario == null) {
                        throw new TokenInvalidoException("Problema interno ao criar token: usuario nulo.");
                }
                if (StringUtils.isBlank(token)) {
                        throw new TokenInvalidoException("Problema interno ao criar token: token vazio.");
                }
                if (proximaDataExpiracao == null) {
                        throw new TokenInvalidoException("Problema interno ao criar token: proximaDataExpiracao nula.");
                }

                SegurancaAPI segurancaAPI = this.retornarPorUsuario(usuario);
                if (segurancaAPI == null) {
                        segurancaAPI = new SegurancaAPI(token, proximaDataExpiracao, usuario);
                } else {
                        segurancaAPI.atualizarToken(token, proximaDataExpiracao);
                }

                segurancaAPI = this.segurancaRepository.save(segurancaAPI);
                
                if (segurancaAPI.isSalvo()) {
                        if (segurancaAPI.expirado()) {
                                throw new TokenExpiradoException("Token de acesso expirado. Gere um novo token e tente novamente.");
                        }
                        else {
                                throw new TokenCriadoException(segurancaAPI);
                        }
                }
        }

        private Date retornarProximaDataExpiracao() {
                Date agora = new Date();
                int dia = DateUtilsApostas.retornaUnidade(agora, DateUtilsApostas.DIA);
                int mes = DateUtilsApostas.retornaUnidade(agora, DateUtilsApostas.MES);
                int ano = DateUtilsApostas.retornaUnidade(agora, DateUtilsApostas.ANO);
                return DateUtilsApostas.retornaData(dia + "/" + mes + "/" + ano + " 23:59:59", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
        }

        private void validarAcessoAplicativo(String appClientId, String appClientSecret) throws TokenInvalidoException {
                if (StringUtils.isBlank(appClientId)) {
                        throw new TokenInvalidoException("Atributo clientId nulo.");
                }
                if (StringUtils.isBlank(appClientId)) {
                        throw new TokenInvalidoException("Atributo clientSecret nulo.");
                }
                if (!appClientId.equalsIgnoreCase(APP_CLIENT_ID) && !appClientSecret.equalsIgnoreCase(APP_CLIENT_PASSWD)) {
                        throw new TokenInvalidoException("Seguranca: aplicativo nao autorizado.");
                }
        }
        
        private Usuario retornarPorLoginESenha(String login, String senha) throws UsuarioOuSenhaInvalidaException{
                Usuario  usuario = this.usuarioRepository.findByLoginAndSenha(login, FormatadorUtil.encryptMD5(senha));
                if(usuario == null){
                        throw new UsuarioOuSenhaInvalidaException("Usuário não encontrado.");
                }else{
                        Hibernate.initialize(usuario.getPerfil().getPermissoes());
                }
                return usuario;
        }
        
        public RetornoLoginForm retornarErroOAuth(int errorCode, String error, Exception e, HttpServletResponse response) {
                String descricao = error;
                if(e!=null){
                        descricao = e.getMessage()!=null ? e.getMessage() : "NullPointerException";
                }

                new OptionsFilter().configCorsResponse(response);
                response.setStatus(errorCode);
                return new RetornoLoginForm(error, descricao, errorCode);
	}
        
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public RetornoLoginForm logarOAuth(HttpServletRequest request, HttpServletResponse response) {
                try {
                        OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
                        String appClientId = oauthRequest.getClientId();
                        String appClientSecret = oauthRequest.getClientSecret();

                        try {
                                this.validarAcessoAplicativo(appClientId, appClientSecret);
                        } catch (TokenInvalidoException e) {
                                return this.retornarErroOAuth(HttpServletResponse.SC_FORBIDDEN, "Aplicativo cliente não autorizado.", e, response);
                        }
                        
                        String senha = oauthRequest.getPassword();
                        String login = oauthRequest.getUsername();
                        Usuario usuario = null;
                        
                        try {
                                usuario = this.retornarPorLoginESenha(login, senha);
                        } catch (UsuarioOuSenhaInvalidaException e) {
                                return this.retornarErroOAuth(HttpServletResponse.SC_FORBIDDEN, "Login e/ou senha inválida.", e, response);
                        }
                        
                        String accessToken = new OAuthIssuerImpl(new MD5Generator()).accessToken();
                        Date proximaDataExpiracao = this.retornarProximaDataExpiracao();
                        
                        try {
                                this.atualizarToken(usuario, accessToken, proximaDataExpiracao);
                        } catch (TokenExpiradoException e) {
                                return this.retornarErroOAuth(HttpServletResponse.SC_FORBIDDEN, "Token expirado.", e, response);
                        } catch (TokenInvalidoException e) {
                                return this.retornarErroOAuth(HttpServletResponse.SC_BAD_REQUEST, "Token inválido.", e, response);
                        } catch (TokenCriadoException e) {
                                //token jah criado anteriormente, somente retorna.
                                proximaDataExpiracao = e.getSegurancaAPI().getExpiracaoToken();
                        }
                        
                        return new RetornoLoginForm(accessToken, this.transformarProximaDataExpiracaoEmSegundos(new Date(), proximaDataExpiracao), usuario);

                } catch (OAuthProblemException e) {
                        return this.retornarErroOAuth(HttpServletResponse.SC_FORBIDDEN, CodeResponse.INVALID_REQUEST, e, response);
                } catch (Exception e) {
                        e.printStackTrace();
                        return this.retornarErroOAuth(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, CodeResponse.SERVER_ERROR, e, response);
                }
        }

        private String transformarProximaDataExpiracaoEmSegundos(Date atual, Date proxima) {
                int horas = DateUtilsApostas.getDiferencaHoras(atual, proxima);
                return "" + (horas * 60 * 60);
        }

        public SegurancaAPI getUsuarioLogado() throws TokenInvalidoException {
                SegurancaAPI seg = SegurancaAPIThreadLocal.getSegurancaAPI();
                if (seg == null) {
                        throw new TokenInvalidoException("Usuário não logado.");
                } else {
                        return seg;
                }
        }

}