package com.jopss.apostas.excecoes;

public class DataNaoPermitidaException extends ApostasException{
        
        public DataNaoPermitidaException() {
                super();
        }
        
        public DataNaoPermitidaException(String message) {
                super(message);
        }

        public DataNaoPermitidaException(Throwable cause) {
                super(cause);
        }
        
}
