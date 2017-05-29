package br.com.sweetpet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller responsável por atender a pagina inicial
 * @author Vinicius Cunha
 */
@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}

}
