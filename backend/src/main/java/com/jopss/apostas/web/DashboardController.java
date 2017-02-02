package com.jopss.apostas.web;

import com.jopss.apostas.web.util.ApostasController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController extends ApostasController {
	
        @RequestMapping(value = "/", method = RequestMethod.GET)
	public String abrir() {
		return "dashboard";
	}
        
}
