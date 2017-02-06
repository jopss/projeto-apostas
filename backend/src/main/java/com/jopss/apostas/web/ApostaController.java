package com.jopss.apostas.web;

import com.jopss.apostas.excecoes.ApostasException;
import com.jopss.apostas.modelos.Aposta;
import com.jopss.apostas.modelos.enums.RoleEnum;
import com.jopss.apostas.servicos.security.annotation.Privado;
import com.jopss.apostas.servicos.security.annotation.Publico;
import com.jopss.apostas.web.form.ApostaForm;
import com.jopss.apostas.web.form.Resposta;
import com.jopss.apostas.web.util.ApostasController;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApostaController extends ApostasController {

        @Publico
        @ResponseBody
        @RequestMapping(value = "/teste/", method = RequestMethod.GET)
        public Resposta teste(HttpServletResponse resp) {
                Resposta resposta = new Resposta();
                resposta.setMensagemSucesso("mensagem.teste", resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_ADMIN)
        @ResponseBody
        @RequestMapping(value = "/aposta/{id}", method = RequestMethod.DELETE)
        public Resposta deletar(@PathVariable Long id, HttpServletResponse resp) throws ApostasException {
                new Aposta(id).remover();
                Resposta resposta = new Resposta();
                resposta.setMensagemSucesso("aposta.sucesso.deletar", resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_ADMIN)
        @ResponseBody
        @RequestMapping(value = "/aposta/{id}", method = RequestMethod.GET)
        public Resposta editar(@PathVariable Long id, HttpServletResponse resp) {
                Resposta resposta = new Resposta();
                resposta.setDado(new Aposta(id).buscarPorId(), resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_ADMIN)
        @ResponseBody
        @RequestMapping(value = "/aposta/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public Resposta salvar(@RequestBody Aposta aposta, HttpServletResponse resp) throws ApostasException {
                aposta = aposta.salvar();
                Resposta resposta = new Resposta();
                resposta.setDado(aposta, resp, "aposta.sucesso");
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_GERAL)
        @ResponseBody
        @RequestMapping(value = "/apostas/pagina", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
        public Resposta buscarPorPagina(@RequestBody ApostaForm form, HttpServletResponse resp) throws ApostasException {
                Resposta resposta = new Resposta();
                resposta.setLista((new Aposta()).buscarRegistroPaginado(form), resp);
                resposta.setDado(form);
                return resposta;
        }

}
