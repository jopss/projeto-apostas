package com.jopss.apostas.web.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jopss.apostas.util.JsonDateDeserializer;
import com.jopss.apostas.util.JsonDateSerializer;
import java.util.Date;

public class ApostaForm extends PaginacaoForm{

        @JsonSerialize(using=JsonDateSerializer.class)
        @JsonDeserialize(using=JsonDateDeserializer.class)
        private Date dataInicial;
        
        @JsonSerialize(using=JsonDateSerializer.class)
        @JsonDeserialize(using=JsonDateDeserializer.class)
        private Date dataFinal;
        
        private String descricao;

        public Date getDataInicial() {
                return dataInicial;
        }

        public void setDataInicial(Date dataInicial) {
                this.dataInicial = dataInicial;
        }

        public Date getDataFinal() {
                return dataFinal;
        }

        public void setDataFinal(Date dataFinal) {
                this.dataFinal = dataFinal;
        }

        public String getDescricao() {
                return descricao;
        }

        public void setDescricao(String Descricao) {
                this.descricao = Descricao;
        }
        
}
