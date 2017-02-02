package com.jopss.apostas.excecoes;

public class ApostasException extends Exception {

        public ApostasException() {
                super();
        }
        
        public ApostasException(String message) {
                super(message);
        }

        public ApostasException(Throwable cause) {
                super(cause);
        }
        
}
