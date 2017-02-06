package com.jopss.apostas.servicos.repositorio;

import com.jopss.apostas.modelos.SegurancaAPI;
import com.jopss.apostas.modelos.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SegurancaRepository extends CrudRepository<SegurancaAPI, Long> {
        
        @Query("SELECT s FROM SegurancaAPI s WHERE s.token = :token")
        SegurancaAPI findByToken(@Param("token") String token);
        
        SegurancaAPI findByUsuario(Usuario usuario);
        
}