package com.jopss.apostas.servicos.security;

import com.jopss.apostas.modelos.SegurancaAPI;

public class SegurancaAPIThreadLocal {

	private static final ThreadLocal<SegurancaAPI> threadLocal = new ThreadLocal<>();

	public static void setSegurancaAPI(SegurancaAPI segurancaAPI) {
		threadLocal.remove();
		threadLocal.set(segurancaAPI);
	}

	public static SegurancaAPI getSegurancaAPI() {
		return threadLocal.get();
	}
}