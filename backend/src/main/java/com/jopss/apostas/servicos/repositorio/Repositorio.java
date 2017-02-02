package com.jopss.apostas.servicos.repositorio;

import com.jopss.apostas.util.*;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class Repositorio implements Serializable {

        protected static final Logger logger = Logger.getLogger(Repositorio.class);
        private static final long serialVersionUID = -1340481266616282366L;
        
        @PersistenceContext
        private EntityManager em;

        EntityManager getEntityManager() {
                return em;
        }
        
        public Page buscarPaginado(HQLGenerator queryGenerator, Pageable pageRequest){
                Query query = em.createQuery(queryGenerator.getQuery());
                this.configurarPaginacao(query, pageRequest);
                queryGenerator.setParameters(query);
                return new PageImpl(query.getResultList(), pageRequest, countRegistros(queryGenerator));
        }
                
        private void configurarPaginacao(Query query, Pageable pageRequest){
                int inicio = pageRequest.getPageSize()*pageRequest.getPageNumber();
                query.setFirstResult(inicio);
                query.setMaxResults(pageRequest.getPageSize());
        }
        
        
        private Long countRegistros(HQLGenerator queryGenerator) {
                Query query = getEntityManager().createQuery(queryGenerator.getCountQuery());
                queryGenerator.setParameters(query);
                return (Long) query.getSingleResult();
        }

}
