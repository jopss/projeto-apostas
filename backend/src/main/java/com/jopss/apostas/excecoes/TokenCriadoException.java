package com.jopss.apostas.excecoes;

import com.jopss.apostas.modelos.SegurancaAPI;

public class TokenCriadoException extends ApostasException {

        private SegurancaAPI segurancaAPI;
        
        public TokenCriadoException(SegurancaAPI segurancaAPI) {
                super("Token ja criado para este usuario.");
                this.segurancaAPI = segurancaAPI;
        }

        public SegurancaAPI getSegurancaAPI() {
                return segurancaAPI;
        }
}