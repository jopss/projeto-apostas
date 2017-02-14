package com.jopss.apostas.web.util;

import com.jopss.apostas.excecoes.ApostasException;
import com.jopss.apostas.util.NumbersUtils;
import com.jopss.apostas.servicos.ParametrosSistema;
import com.jopss.apostas.servicos.security.SegurancaServico;
import com.jopss.apostas.web.form.Resposta;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.PropertyValuesEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Superclasse de todo Controlador do sistema.
 */
public abstract class ApostasController {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
        @Autowired
        protected ParametrosSistema parametrosSistema;
        
        @Autowired
        protected SegurancaServico segurancaServico;
        
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(BigDecimal.class, new PropertyValuesEditor() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(NumbersUtils.convertStringToBigDecimal(text));
			}
			@Override
			public String getAsText() {
				return NumbersUtils.convertBigDecimalToStringFormat((BigDecimal) getValue());
			}
		});
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
	}
        
	@ExceptionHandler(value = { Exception.class })
	public Resposta handleConflict(Exception e, HttpServletResponse response) {
		Resposta resposta = new Resposta();
		if (e == null) {
                        resposta.addErro("NullPointerException interno...", response);
		} 
//                else if (e instanceof SegurancaException) {
//                        resposta.addErroLogin(e.);
//			resposta = end.retornarErroLogin((SegurancaException) e, response);
//		} 
                else if (e instanceof TransactionSystemException){
                        log.error(e);
                        resposta.addErros((TransactionSystemException)e, response);
                }
                else if (e instanceof ApostasException){
                        log.error(e);
                        resposta.addValidacao(e.getMessage(), response);
                }
                else {
                        log.error(e);
                        resposta.addErroGenerico(e, response);
		}
		return resposta;
	}
        
}
