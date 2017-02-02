package com.jopss.apostas.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jopss.apostas.modelos.enums.RoleEnum;
import com.jopss.apostas.util.Modelos;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Permissao extends Modelos {
        
        private static final long serialVersionUID = 8765060059417187982L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "permissaoGenerator")
	@TableGenerator(name = "permissaoGenerator", allocationSize = 1)
	private Long id;
        
        @NotEmpty
        private String nome;
        
        private String descricao;
        
        @NotNull
        @Enumerated(EnumType.STRING)
        private RoleEnum papel;
        
        @JsonIgnore
        @OneToOne
        private Permissao permissao_pai;

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

        public String getDescricao() {
                return descricao;
        }

        public void setDescricao(String descricao) {
                this.descricao = descricao;
        }

        public Permissao getPermissao_pai() {
                return permissao_pai;
        }

        public void setPermissao_pai(Permissao permissao_pai) {
                this.permissao_pai = permissao_pai;
        }

        public RoleEnum getPapel() {
                return papel;
        }

        public void setPapel(RoleEnum papel) {
                this.papel = papel;
        }
        
}
