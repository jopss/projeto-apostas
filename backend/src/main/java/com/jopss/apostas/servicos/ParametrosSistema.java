package com.jopss.apostas.servicos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParametrosSistema {

	private static final String ENV_DEVELOP = "DESENVOLVIMENTO";
	private static final String ENV_PRODUCTION = "PRODUCAO";

	@Value("#{properties['ambiente']}")
	private String ambiente;

	/**
	 * Verifica se o tipo de ambiente implantado eh desenvolvimento.
	 * OBS: usar com muita cautela, ou nem usar....
	 */
	public boolean isDesenvolvimento() {
		return ENV_DEVELOP.equalsIgnoreCase(ambiente);
	}
	
	/**
	 * Verifica se o tipo de ambiente implantado eh producao.
	 * OBS: usar com muita cautela, ou nem usar....
	 */
	public boolean isProducao() {
		return ENV_PRODUCTION.equalsIgnoreCase(ambiente);
	}

}
