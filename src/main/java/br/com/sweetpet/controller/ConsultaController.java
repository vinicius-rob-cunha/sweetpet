package br.com.sweetpet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sweetpet.model.Consulta;
import br.com.sweetpet.service.AnimalService;
import br.com.sweetpet.service.ConsultaService;

/**
 * <p>Controller responsável por responder todas requisições relacionadas a consultas pela URI {@code /consultas}</p>
 * <ul>
 * 	<li>[GET ] / - lista - recupera todas as consultas e abre a tela de listagem</li>
 *  <li>[GET ] /cadastro - cadastro - recupera os animais e abre o cadastro de consulta</li>
 *  <li>[POST] / - salvar - valida os dados da requisição e grava a consulta no banco, retornando para a lista</li>
 *  <li>[GET ] /{id} - detalhes - abre a tela de cadastro de consulta já preenchida</li>
 * </ul>
 * @author Vinicius Cunha
 */
@Controller
@RequestMapping("/consultas")
public class ConsultaController {

	@Autowired
	private ConsultaService service;
	
	@Autowired
	private AnimalService animalService;

	@GetMapping
	public ModelAndView lista() {
		ModelAndView mv = new ModelAndView("consultas");
		mv.addObject("consultas", service.findAll());
		return mv;
	}
	
	@GetMapping("/cadastro")
	public ModelAndView cadastro(Consulta consulta) {
		ModelAndView mv = new ModelAndView("consulta_cadastro");
		mv.addObject("animais", animalService.findAll());
		return mv;
	}
	
	@PostMapping
	public ModelAndView salvar(@Valid Consulta consulta, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()){
			return cadastro(consulta);
		}
		
		service.save(consulta);
		
		redirectAttributes.addFlashAttribute("success", "Consulta registrada com sucesso");
		return new ModelAndView("redirect:/consultas");
	}
	
	@GetMapping("/{id}")
	public ModelAndView detalhes(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("consulta_cadastro");
		mv.addObject("consulta", service.findOne(id));
		mv.addObject("animais", animalService.findAll());
		return mv;
	}
	
}
