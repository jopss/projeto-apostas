package com.jopss.apostas.web;

import com.jopss.apostas.modelos.Usuario;
import com.jopss.apostas.web.form.Resposta;
import com.jopss.apostas.web.form.RespostaSessao;
import com.jopss.apostas.web.util.ApostasController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioController extends ApostasController{
        
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String abrir() {
		return "usuario/cadastro";
	}
        
        /**
         * Acao que verifica periodicamente se ha usuario logado.
         */
        @ResponseBody
        @RequestMapping(value = "/verificar/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public RespostaSessao verificar(HttpServletResponse resp) {
                RespostaSessao resposta = new RespostaSessao();
                resposta.addUsuario( super.sessionUserSupport.getUsuarioLogado(), resp );
		return resposta;
	}
        
        @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
	public Resposta salvar(@RequestBody Usuario usuario, HttpServletResponse resp, HttpSession session) {
                Resposta resposta = new Resposta();
                try {
                        usuario = usuario.salvar();
                        resposta.setDado(usuario, resp, "usuario.sucesso");
                        
                } catch (TransactionSystemException ex) {
                        log.error(ex);
                        resposta.addErros(ex, resp);
                } catch (Exception ex) {
                        log.error(ex);
                        resposta.addErroGenerico(ex, resp);
                }
                return resposta;
	}
        
        @RequestMapping(value = "/todos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
	public Resposta buscarTodos(HttpServletResponse resp, HttpSession session) {
                Resposta resposta = new Resposta();
                try {
                        resposta.setLista((new Usuario()).buscarTodos(), resp);
                } catch (Exception ex) {
                        log.error(ex);
                        resposta.addErroGenerico(ex, resp);
                }
                return resposta;
	}
        
        @RequestMapping(value = "/logado", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
	public Resposta buscarLogado(HttpServletResponse resp, HttpSession session) {
                Resposta resposta = new Resposta();
                try {
                        Usuario usu = super.sessionUserSupport.getUsuarioLogado();
                        usu.setSenha(null);
                        
                        resposta.setDado(usu, resp);
                } catch (Exception ex) {
                        log.error(ex);
                        resposta.addErroGenerico(ex, resp);
                }
                return resposta;
	}
        
}
