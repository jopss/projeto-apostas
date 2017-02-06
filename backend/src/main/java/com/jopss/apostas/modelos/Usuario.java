package com.jopss.apostas.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jopss.apostas.servicos.repositorio.UsuarioRepository;
import com.jopss.apostas.util.FormatadorUtil;
import com.jopss.apostas.util.Modelos;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Size;
import org.apache.commons.collections.IteratorUtils;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@NamedEntityGraph(name = "perfil.permissoes", attributeNodes = @NamedAttributeNode(value = "perfil", subgraph = "permissoes"), 
               subgraphs = @NamedSubgraph(name = "permissoes", attributeNodes = @NamedAttributeNode("permissoes")))
public class Usuario extends Modelos {
        
        private static final long serialVersionUID = 8765060059417187982L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "usuarioGenerator")
	@TableGenerator(name = "usuarioGenerator", allocationSize = 1)
	private Long id;
        
        @NotEmpty
        private String nome;
        
        @NotEmpty
        @Size(min = 1, max = 20)
        @Column(unique = true)
        private String login;
        
        @NotEmpty
        @Size(min = 1)
        private String senha;
        
        @JsonIgnore //por seguranca tira os perfis das consultas.
        @ManyToOne(optional = false)
        private Perfil perfil;

        public Usuario() {
        }

        public Usuario(Long id) {
                this.id = id;
        }

        public Usuario(String login) {
                this.login = login;
        }
        
        @Override
        protected UsuarioRepository getRepository(){
                return (UsuarioRepository) super.getRepository();
        }
        
        public Usuario buscarPorId() {
		return this.getRepository().findOne(id);
	}
        
        public Usuario buscarPorLogin(){
                return this.getRepository().findByLogin(login);
        }
        
        public List<Usuario> buscarTodos(){
                return IteratorUtils.toList(this.getRepository().findAll().iterator());
        }
        
        /**
         * Regras de unicidade de login e obrigatoriedade está ou banco.
         */
        public Usuario salvar() {
                this.setSenha(FormatadorUtil.encryptMD5(this.getSenha()) );
                return this.getRepository().save(this);
        }
        
        @Override
        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getLogin() {
                return login;
        }

        public void setLogin(String login) {
                this.login = login;
        }

        public String getSenha() {
                return senha;
        }

        public void setSenha(String senha) {
                this.senha = senha;
        }

        public Perfil getPerfil() {
                return perfil;
        }
        
}
