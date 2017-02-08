package com.jopss.apostas.web.util;

import com.jopss.apostas.web.form.LoginForm;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Possibilita a adicionar ou alterar parametros da request.
 */
public class PrettyWrappedRequestLoginOauth extends HttpServletRequestWrapper {
	private final Map<String, String[]> modifiableParameters;
	private Map<String, String[]> allParameters = null;
	
        public PrettyWrappedRequestLoginOauth(final HttpServletRequest request, final LoginForm loginForm) throws IllegalArgumentException, IllegalAccessException {
		super(request);
		modifiableParameters = new TreeMap<String, String[]>();
		Class<? extends LoginForm> clazz = loginForm.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			String chave = f.getName();
			String valor = f.get(loginForm) != null ? f.get(loginForm).toString() : null;
			modifiableParameters.put(chave, new String[] { valor });
		}
	}
        
	@Override
	public String getContentType() {
		return "application/x-www-form-urlencoded";
	}
        
	@Override
	public String getParameter(final String name) {
		String[] strings = getParameterMap().get(name);
		if (strings != null) {
			return strings[0];
		}
		return super.getParameter(name);
	}
        
	@Override
	public Map<String, String[]> getParameterMap() {
		if (allParameters == null) {
			allParameters = new TreeMap<String, String[]>();
			allParameters.putAll(super.getParameterMap());
			allParameters.putAll(modifiableParameters);
		}
		return Collections.unmodifiableMap(allParameters);
	}
        
	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}
        
	@Override
	public String[] getParameterValues(final String name) {
		return getParameterMap().get(name);
	}
}
