package com.jopss.apostas.web;

import com.jopss.apostas.excecoes.ApostasException;
import com.jopss.apostas.modelos.Aposta;
import com.jopss.apostas.modelos.enums.RoleEnum;
import com.jopss.apostas.servicos.repositorio.ApostaRepository;
import com.jopss.apostas.servicos.security.annotation.Privado;
import com.jopss.apostas.servicos.security.annotation.Publico;
import com.jopss.apostas.web.form.ApostaForm;
import com.jopss.apostas.web.form.Resposta;
import com.jopss.apostas.web.util.ApostasController;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/aposta")
public class ApostaController extends ApostasController {
        
        @Autowired
        private ApostaRepository apostaRepository;

        @Publico
        @ResponseBody
        @RequestMapping(value = "/teste", method = RequestMethod.GET)
        public Resposta teste(HttpServletResponse resp) {
                Resposta resposta = new Resposta();
                resposta.setMensagemSucesso("mensagem.teste", resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_ADMIN)
        @ResponseBody
        @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
        public Resposta deletar(@PathVariable Long id, HttpServletResponse resp) throws ApostasException {
                Aposta aposta = this.apostaRepository.findById(id);
                aposta.limparListaPalpites();
                this.apostaRepository.delete(aposta);
                
                Resposta resposta = new Resposta();
                resposta.setMensagemSucesso("aposta.sucesso.deletar", resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_ADMIN)
        @ResponseBody
        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public Resposta editar(@PathVariable Long id, HttpServletResponse resp) {
                Aposta aposta = this.apostaRepository.findById(id);
                
                Resposta resposta = new Resposta();
                resposta.setDado(aposta, resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_ADMIN)
        @ResponseBody
        @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public Resposta salvar(@RequestBody Aposta aposta, HttpServletResponse resp) throws ApostasException {
                aposta.validarDataFinalizacao();
                aposta.preencherPalpites();
                aposta = this.apostaRepository.save(aposta);
                
                Resposta resposta = new Resposta();
                resposta.setDado(aposta, resp, "aposta.sucesso");
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_GERAL)
        @ResponseBody
        @RequestMapping(value = "/pagina", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
        public Resposta buscarPorPagina(@RequestBody ApostaForm form, HttpServletResponse resp) throws ApostasException {
                form.validarDatas();
                
                PageRequest pageRequest = form.getPageRequest();
                Page<Aposta> pagina;
                if (form.getDataInicial() != null && form.getDataFinal() != null) {
                        pagina = this.apostaRepository.findByDateFinalizacaoBetweenAndDescricao(
                                form.getDataInicial(), form.getDataFinal(),form.getDescricao() , pageRequest);

                } else {
                        pagina = this.apostaRepository.findAll(pageRequest);
                }

                form.setTotalRegistros(pagina.getTotalElements());
                List<Aposta> lista = pagina.getContent();
                
                Resposta resposta = new Resposta();
                resposta.setLista(lista, resp);
                resposta.setDado(form);
                return resposta;
        }

}
