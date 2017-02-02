package com.jopss.apostas.web.form;

import java.io.Serializable;
import org.springframework.data.domain.PageRequest;

public class PaginacaoForm implements Serializable {

        protected Integer paginaAtual;
        protected Integer quantidadeRegistro;
        protected Long totalRegistros;

        public Integer getPaginaAtual() {
                return paginaAtual;
        }

        public void setPaginaAtual(Integer paginaAtual) {
                this.paginaAtual = paginaAtual;
        }

        public Integer getQuantidadeRegistro() {
                return quantidadeRegistro;
        }

        public void setQuantidadeRegistro(Integer quantidadeRegistro) {
                this.quantidadeRegistro = quantidadeRegistro;
        }

        public Long getTotalRegistros() {
                return totalRegistros;
        }

        public void setTotalRegistros(Long totalRegistros) {
                this.totalRegistros = totalRegistros;
        }
        
        public PageRequest getPageRequest(){
                return new PageRequest(this.paginaAtual-1, this.quantidadeRegistro);
        }
        
}
