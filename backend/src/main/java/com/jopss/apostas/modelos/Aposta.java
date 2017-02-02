package com.jopss.apostas.modelos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jopss.apostas.excecoes.ApostasException;
import com.jopss.apostas.excecoes.DataNaoPermitidaException;
import com.jopss.apostas.servicos.repositorio.ApostaRepository;
import com.jopss.apostas.util.DateUtilsApostas;
import com.jopss.apostas.util.JsonDateDeserializer;
import com.jopss.apostas.util.JsonDateSerializer;
import com.jopss.apostas.util.Modelos;
import com.jopss.apostas.web.form.ApostaForm;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections.IteratorUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Entity
@NamedEntityGraph(name = "palpites", attributeNodes = @NamedAttributeNode("palpites"))
public class Aposta extends Modelos {

        private static final long serialVersionUID = 8765060059417187982L;

        @Id
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "apostaGenerator")
        @TableGenerator(name = "apostaGenerator", allocationSize = 1)
        private Long id;

        @NotEmpty
        private String descricao;

        @Future
        @NotNull
        @Temporal(TemporalType.DATE)
        @JsonSerialize(using = JsonDateSerializer.class)
        @JsonDeserialize(using = JsonDateDeserializer.class)
        private Date dateFinalizacao;

        @OneToMany(mappedBy = "aposta", orphanRemoval = true, cascade = CascadeType.ALL)
        private List<Palpite> palpites;

        public Aposta() {
        }

        public Aposta(Long id) {
                this.id = id;
        }

        @Override
        protected ApostaRepository getRepository() {
                return (ApostaRepository) super.getRepository();
        }

        public Aposta buscarPorId() {
                return this.getRepository().findById(this.getId());
        }

        public List<Aposta> buscarRegistroPaginado(ApostaForm form) throws ApostasException {
                PageRequest pageRequest = form.getPageRequest();
                Page<Aposta> pagina;
                if (form.getDataInicial() != null && form.getDataFinal() != null) {
                        if (form.getDataInicial().after(form.getDataFinal())) {
                                throw new DataNaoPermitidaException("aposta.falha.intervalo_data_invalido");
                        }

                        pagina = this.getRepository().findByDateFinalizacaoBetweenAndDescricao(
                                form.getDataInicial(), form.getDataFinal(),form.getDescricao() , pageRequest);

                } else {
                        pagina = this.getRepository().findAll(pageRequest);
                }

                form.setTotalRegistros(pagina.getTotalElements());
                List<Aposta> ret = pagina.getContent();
                return ret;
        }

        public Aposta salvar() throws ApostasException {

                if (this.dateFinalizacao.before(DateUtilsApostas.arredondaDataZerandoHora(new Date()))) {
                        throw new DataNaoPermitidaException("aposta.falha.data_nao_permitida");
                }

                for (Palpite palpite : this.getPalpites()) {
                        palpite.setAposta(this);
                }
                return this.getRepository().save(this);
        }

        public void remover() throws ApostasException {
                Aposta aposta = this.buscarPorId();
                if (aposta.getPalpites() != null) {
                        aposta.getPalpites().clear(); //forca o cascade
                }
                this.getRepository().delete(aposta);
        }

        @Override
        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getDescricao() {
                return descricao;
        }

        public void setDescricao(String descricao) {
                this.descricao = descricao;
        }

        public Date getDateFinalizacao() {
                return dateFinalizacao;
        }

        public void setDateFinalizacao(Date dateFinalizacao) {
                this.dateFinalizacao = dateFinalizacao;
        }

        public List<Palpite> getPalpites() {
                return palpites;
        }

        public void setPalpites(List<Palpite> palpites) {
                this.palpites = palpites;
        }
}
