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

import br.com.sweetpet.model.Animal;
import br.com.sweetpet.model.TipoAnimal;
import br.com.sweetpet.model.Vacina;
import br.com.sweetpet.service.AnimalService;

/**
 * <p>Controller responsável por responder todas requisições relacionadas a animais e suas vacinas pela URI {@code /animais}</p>
 * <ul>
 * 	<li>[GET ] / - lista - recupera todos os animais e abre a tela de listagem</li>
 *  <li>[GET ] /cadastro - cadastro - recupera os tipos de animais e abre o cadastro de consulta</li>
 *  <li>[POST] / - salvar - valida os dados da requisição e grava o animal no banco, retornando para a lista</li>
 *  <li>[GET ] /{id} - detalhes - abre a tela de cadastro de animal já preenchida</li>
 *  <li>[GET ] /remove/{id} - remove - exclui o animal e volta para a tela de lista</li>
 *  <li>[GET ] /{id}/vacina - vacinas - carrega o animal e abre a lista de vacinas</li>
 *  <li>[POST] /{id}/vacina - salvarVacina - salva a vacina no banco e retorna para a mesma tela</li>
 *  <li>[GET ] /{id}/vacina/{id_vacina}/confirma - aplicaVacina - altera o status da vacina para aplicada</li>
 *  <li>[GET ] /{id}/vacina/{id_vacina}/remove - removeVacina - exclui a vacina</li>
 * </ul>
 * @author Vinicius Cunha
 */
@Controller
@RequestMapping("/animais")
public class AnimalController {
	
	@Autowired
	private AnimalService service;

	@GetMapping
	public ModelAndView lista() {
		ModelAndView mv = new ModelAndView("animais");
		mv.addObject("animais", service.findAll());
		return mv;
	}
	
	@GetMapping("/cadastro")
	public ModelAndView cadastro(Animal animal) {
		ModelAndView mv = new ModelAndView("animal_cadastro");
		mv.addObject("tipos", TipoAnimal.values());
		return mv;
	}
	
	@PostMapping
	public ModelAndView salvar(@Valid Animal animal, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()){
			return cadastro(animal);
		}
		
		service.save(animal);
		
		redirectAttributes.addFlashAttribute("success", "Cadastro do(a) " + animal.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/animais");
	}
	
	@GetMapping("/{id}")
	public ModelAndView detalhes(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("animal_cadastro");
		mv.addObject("animal", service.findOne(id));
		mv.addObject("tipos", TipoAnimal.values());
		return mv;
	}
	
	@GetMapping("/remove/{id}")
	public String remove(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Animal animal = service.findOne(id);
		
		service.delete(id);
		
		redirectAttributes.addFlashAttribute("success", animal.getNome() + " removido com sucesso");
		return "redirect:/animais";
	}
	
	@GetMapping("/{id}/vacina")
	public ModelAndView vacinas(@PathVariable("id") Long id, Vacina vacina) {
		Animal animal = service.findOne(id);
		
		ModelAndView mv = new ModelAndView("vacinas");
		mv.addObject("animal", animal);
		return mv;
	}
	
	@PostMapping("/{id}/vacina")
	public ModelAndView salvarVacina(@PathVariable("id") Long id, @Valid Vacina vacina, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()){
			return vacinas(id, vacina);
		}
		
		service.adicionaVacina(id, vacina);
		
		redirectAttributes.addFlashAttribute("vacina_alterada", vacina.getId());
		return new ModelAndView("redirect:/animais/" + id + "/vacina");
	}
	
	@GetMapping("/{id}/vacina/{id_vacina}/confirma")
	public String aplicaVacina(@PathVariable("id") Long id, @PathVariable("id_vacina") Long vacinaId, RedirectAttributes redirectAttributes) {
		service.confirmaVacina(vacinaId);
		redirectAttributes.addFlashAttribute("vacina_alterada", vacinaId);
		return "redirect:/animais/" + id + "/vacina";
	}
	
	@GetMapping("/{id}/vacina/{id_vacina}/remove")
	public String removeVacina(@PathVariable("id") Long id, @PathVariable("id_vacina") Long vacinaId) {
		service.removeVacina(vacinaId);
		return "redirect:/animais/" + id + "/vacina";
	}
	
}
