package com.jopss.apostas.web.form;

import com.jopss.apostas.modelos.Perfil;
import com.jopss.apostas.modelos.Usuario;
import javax.servlet.http.HttpServletResponse;

/**
 * Objeto de resposta para a verificacao de sessao e permissoes.
 * Contem alem dos dados da Resposta, um atributo de perfil do usuario logado.
 */
public class RespostaSessao extends Resposta {
        
        private Perfil perfil; 

        public void addUsuario(Usuario logado, HttpServletResponse resp){
                if(logado == null){
                        super.addErroLogin("NOK", resp);
                }else{
                        super.setDado(logado, resp, "OK");
                        this.perfil = logado.getPerfil();
                }
        }

        public Perfil getPerfil() {
                return perfil;
        }
        
}
