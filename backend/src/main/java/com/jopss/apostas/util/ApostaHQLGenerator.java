package com.jopss.apostas.util;

import java.util.Date;

public class ApostaHQLGenerator extends HQLGenerator {

        public ApostaHQLGenerator() {
                selectQuery.append("SELECT a ");
                fromQuery.append(" FROM Aposta a ");
        }

        public void filtrarPorDatas(Date dataInicial, Date dataFinal){
                if (dataInicial != null && dataFinal != null) {
                        lWhere.add("a.dateFinalizacao BETWEEN :dataInicial AND :dataFinal");
                        mapParametrosQuery.put("dataInicial", DateUtilsApostas.arredondaDataZerandoHora(dataInicial));
                        mapParametrosQuery.put("dataFinal", DateUtilsApostas.arredondaDataComMaximaHora(dataFinal));
                }
        }
        
        public void filtrarPorDescricao(String descricao){
                if(ValidatorUtil.isNotNullAndNotEmpty(descricao)){
                        lWhere.add("a.descricao like :descricao");
                        mapParametrosQuery.put("descricao", "%"+descricao.trim()+"%");
                }
        }

        public void ordenarPorDataDesc() {
                orderQuery.append(" ORDER BY a.dateFinalizacao DESC ");
        }

}
