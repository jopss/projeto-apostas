package com.jopss.apostas.testes.modelos;

import com.jopss.apostas.excecoes.ApostasException;
import com.jopss.apostas.excecoes.DataNaoPermitidaException;
import com.jopss.apostas.modelos.Aposta;
import com.jopss.apostas.modelos.Palpite;
import com.jopss.apostas.servicos.repositorio.ApostaRepository;
import java.util.ArrayList;
import java.util.Date;
import org.joda.time.DateTime;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApostaTeste {
        
        @Test(expected = DataNaoPermitidaException.class)
        public void tentarSalvarAposDataFinalizacao() throws ApostasException{
                Aposta aposta = new Aposta();
                aposta.setId(1L);
                aposta.setDateFinalizacao(new Date(1L));
                aposta.setPalpites(new ArrayList<Palpite>());
                mockarApostaRepositorio(aposta);
                
                aposta.salvar();
        }
        
        @Test
        public void tentarSalvarAntesDataFinalizacao() throws ApostasException{
                Aposta aposta = new Aposta();
                aposta.setId(1L);
                aposta.setDateFinalizacao((new DateTime()).plusDays(1).toDate());
                aposta.setPalpites(new ArrayList<Palpite>());
                mockarApostaRepositorio(aposta);
                
                aposta.salvar();
        }
        
        private void mockarApostaRepositorio(Aposta aposta) throws ApostasException{
                
                Aposta apostaMockado = new Aposta();
                apostaMockado.setId(1L);
                
                ApostaRepository repoMock = mock(ApostaRepository.class);
                when(repoMock.save(aposta)).thenReturn(apostaMockado);
                
                aposta.setRepository(repoMock);
        }
        
}
