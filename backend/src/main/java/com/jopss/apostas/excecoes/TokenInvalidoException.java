package com.jopss.apostas.excecoes;

public class TokenInvalidoException extends ApostasException {

        public TokenInvalidoException(String message) {
                super(message);
        }

        public TokenInvalidoException(Throwable cause) {
                super(cause);
        }
        
}