package br.com.sweetpet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sweetpet.model.Consulta;
import br.com.sweetpet.model.Veterinario;
import br.com.sweetpet.repository.ConsultaRepository;
import br.com.sweetpet.repository.VeterinarioRepository;

/**
 * Serviço para tratar da lógica de negocio de uma Consulta
 * @author Vinicius Cunha
 */
@Service
public class ConsultaService {
	
	@Autowired
	private ConsultaRepository repo;
	
	@Autowired
	private VeterinarioRepository veterinarioRepo;

	/**
	 * <p>Persiste a consulta e o veterinário no banco</p>
	 * <p>Obs: Caso ainda não exista nenhum veterinário com o mesmo nome, será criado um novo</p> 
	 * @param consulta
	 */
	public Consulta save(Consulta consulta) {
		Veterinario veterinario = processVeterinario(consulta.getVeterinario());
		consulta.setVeterinario(veterinario);
		return repo.save(consulta);
	}

	
	/**
	 * Procura pelo veterinario no banco com o mesmo nome, se existir recupera
	 * caso contrário salva um novo
	 * @param veterinario Responsável pela consulta
	 * @return
	 */
	private Veterinario processVeterinario(Veterinario veterinario) {
		Veterinario cliente = veterinarioRepo.findOneByNomeIgnoreCase(veterinario.getNome()).orElse(veterinario);
		return veterinarioRepo.save(cliente);
	}

	public Iterable<Consulta> findAll() {
		return repo.findAll();
	}

	public Consulta findOne(Long id) {
		return repo.findOne(id);
	}

}
