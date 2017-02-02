package com.jopss.apostas.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

/**
 * Classe mae de todas os modelos. Auxilia na estrategia de banco de dados,
 * escondendo objetos tecnicos dos modelos, ou acoes simples e repetitivas.
 *
 * A criacao do EntityManager e das Transacoes estao gerenciados manualmente (ao
 * inves de delegar ao container), pois os modelos nao sao gerenciados pelo
 * Spring.
 */
@MappedSuperclass
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class Modelos implements Serializable {
        
	protected static final Logger logger = Logger.getLogger(Modelos.class);
	private static final long serialVersionUID = -1340481266616282366L;
        
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao = new Date();
        
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtualizacao;
        
	public abstract Long getId();
        
        @Transient
        private CrudRepository repository;
        
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
        
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof Modelos) {
			final Modelos other = (Modelos) obj;
			return new EqualsBuilder().append(getId(), other.getId()).isEquals();
		} else {
			return false;
		}
	}
        
	@Override
	public String toString() {
		return this.getClass().getName()+"[id=" + getId() + "]";
	}
        
        protected <T extends CrudRepository>T getRepository() {
                if(repository != null){
                        return (T) repository;
                }
                try {
                        return (T) AppContextUtil.getApplicationContext().getBean(Class.forName("com.jopss.apostas.servicos.repositorio."+this.getClass().getSimpleName()+"Repository"));
                } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                }
	}
        
	public boolean isSalvo() {
		return getId() != null;
	}

	public boolean isNaoSalvo() {
		return !isSalvo();
	}

        public Date getDataCriacao() {
                return dataCriacao;
        }

        public Date getDataAtualizacao() {
                return dataAtualizacao;
        }

        public void setRepository(CrudRepository repository) {
                this.repository = repository;
        }
        
}
