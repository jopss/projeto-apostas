package com.jopss.apostas.web.form;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.jopss.apostas.modelos.Permissao;
import com.jopss.apostas.modelos.Usuario;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class RetornoLoginForm {
        
        private String token;
	private String expiraEm;
	private String perfil;
	private String nome;
        private String login;

        private String mensagemErro;
	private String mensagemDescricao;
	private Integer status;
        
	private List<String> permissoes = new ArrayList<>();

        public RetornoLoginForm(String token, String expiraEm, Usuario usuario) {
                this(usuario);
		this.token = token;
		this.expiraEm = expiraEm;
	}
        
        public RetornoLoginForm(String mensagemErro, String mensagemDescricao, Integer status) {
		this.mensagemErro = mensagemErro;
		this.mensagemDescricao = mensagemDescricao;
		this.status = status;
	}
        
        public RetornoLoginForm(Usuario usuario) {
		this.perfil = usuario.getPerfil().getNome();
		this.nome = usuario.getNome();
                this.nome = usuario.getLogin();
		this.status = 200;
                
                for(Permissao p : usuario.getPerfil().getPermissoes()){
                        permissoes.add(p.getPapel().name());
                }
	}

        public String getToken() {
                return token;
        }

        public String getExpiraEm() {
                return expiraEm;
        }

        public String getPerfil() {
                return perfil;
        }

        public String getNome() {
                return nome;
        }

        public String getLogin() {
                return login;
        }

        public String getMensagemErro() {
                return mensagemErro;
        }

        public String getMensagemDescricao() {
                return mensagemDescricao;
        }

        public Integer getStatus() {
                return status;
        }

        public List<String> getPermissoes() {
                return permissoes;
        }
        
}
