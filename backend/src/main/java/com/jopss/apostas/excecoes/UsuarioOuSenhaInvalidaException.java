package com.jopss.apostas.excecoes;

public class UsuarioOuSenhaInvalidaException extends ApostasException {

        public UsuarioOuSenhaInvalidaException(String message) {
                super(message);
        }

        public UsuarioOuSenhaInvalidaException(Throwable cause) {
                super(cause);
        }
        
}
