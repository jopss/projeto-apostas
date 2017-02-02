package com.jopss.apostas.servicos.repositorio;

import com.jopss.apostas.modelos.Aposta;
import com.jopss.apostas.util.ApostaHQLGenerator;
import java.util.Date;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ApostaRepositoryImpl implements ApostaRepositoryCustom{

        @Autowired
        private Repositorio repositorio;
        
        @Override
        @Transactional
        public Page<Aposta> findByDateFinalizacaoBetweenAndDescricao(Date dataInicial, Date dataFinal, String descricao, Pageable page) {
                ApostaHQLGenerator queryGenerator = new ApostaHQLGenerator();
                queryGenerator.filtrarPorDatas(dataInicial, dataFinal);
                queryGenerator.filtrarPorDescricao(descricao);
                queryGenerator.ordenarPorDataDesc();
                Page<Aposta> pagina = repositorio.buscarPaginado(queryGenerator, page);
                
                for(Aposta aposta : pagina.getContent()){
                        Hibernate.initialize(aposta.getPalpites());
                }
                return pagina;
        }
}
