package com.jopss.apostas.servicos.repositorio;

import com.jopss.apostas.modelos.Aposta;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApostaRepositoryCustom {
        
        Page<Aposta> findByDateFinalizacaoBetweenAndDescricao(Date dataInicial, Date dataFinal, String descricao, Pageable page);
        
}
