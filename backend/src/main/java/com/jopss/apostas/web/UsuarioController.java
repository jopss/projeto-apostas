package com.jopss.apostas.web;

import com.jopss.apostas.excecoes.TokenInvalidoException;
import com.jopss.apostas.modelos.Usuario;
import com.jopss.apostas.modelos.enums.RoleEnum;
import com.jopss.apostas.servicos.repositorio.UsuarioRepository;
import com.jopss.apostas.servicos.security.annotation.Privado;
import com.jopss.apostas.servicos.security.annotation.Publico;
import com.jopss.apostas.web.form.Resposta;
import com.jopss.apostas.web.form.RespostaLogin;
import com.jopss.apostas.web.util.ApostasController;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.IteratorUtils;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioController extends ApostasController {

        @Autowired
        private UsuarioRepository usuarioRepository;
        
        @Publico
        @ResponseBody
        @RequestMapping(value = "/logar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
        public String logar(HttpServletRequest request) {
                OAuthResponse response = segurancaServico.logarOAuth(request);
                return response.getBody();
        }

        /**
         * Acao que verifica periodicamente se ha usuario logado.
         */
        @Privado(role = RoleEnum.ROLE_GERAL)
        @ResponseBody
        @RequestMapping(value = "/verificar/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public RespostaLogin verificar(HttpServletResponse resp) throws TokenInvalidoException {
                RespostaLogin resposta = new RespostaLogin();
                resposta.addUsuario(super.segurancaServico.getUsuarioLogado().getUsuario(), resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_GERAL)
        @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public Resposta salvar(@RequestBody Usuario usuario, HttpServletResponse resp, HttpSession session) {
                usuario.encriptarSenha();
                usuario = this.usuarioRepository.save(usuario);
                
                Resposta resposta = new Resposta();
                resposta.setDado(usuario, resp, "usuario.sucesso");
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_GERAL)
        @RequestMapping(value = "/todos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public Resposta buscarTodos(HttpServletResponse resp, HttpSession session) {
                List<Usuario> lista = IteratorUtils.toList(this.usuarioRepository.findAll().iterator());
                
                Resposta resposta = new Resposta();
                resposta.setLista(lista, resp);
                return resposta;
        }

        @Privado(role = RoleEnum.ROLE_GERAL)
        @RequestMapping(value = "/logado", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public Resposta buscarLogado(HttpServletResponse resp, HttpSession session) throws TokenInvalidoException {
                Usuario usu = super.segurancaServico.getUsuarioLogado().getUsuario();
                usu.setSenha(null);

                Resposta resposta = new Resposta();
                resposta.setDado(usu, resp);
                return resposta;
        }

}
