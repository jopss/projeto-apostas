package com.jopss.apostas.excecoes;

public class TokenExpiradoException extends ApostasException {

        public TokenExpiradoException(String message) {
                super(message);
        }

        public TokenExpiradoException(Throwable cause) {
                super(cause);
        }
        
}