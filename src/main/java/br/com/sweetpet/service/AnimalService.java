package br.com.sweetpet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sweetpet.model.Animal;
import br.com.sweetpet.model.Cliente;
import br.com.sweetpet.model.StatusVacina;
import br.com.sweetpet.model.Vacina;
import br.com.sweetpet.repository.AnimalRepository;
import br.com.sweetpet.repository.ClienteRepository;
import br.com.sweetpet.repository.VacinaRepository;

/**
 * Serviço para tratar da lógica de negocio de um Animal
 * @author Vinicius Cunha
 */
@Service
public class AnimalService {
	
	@Autowired
	private AnimalRepository repo;
	
	@Autowired
	private ClienteRepository clienteRepo;
	
	@Autowired
	private VacinaRepository vacinaRepo;

	/**
	 * <p>Persiste o animal e seu dono no banco</p>
	 * <p>Obs: Caso ainda não exista nenhum dono com o mesmo nome, será criado um novo</p>
	 * @param animal
	 */
	public Animal save(Animal animal) {
		Cliente dono = processDono(animal.getDono());
		animal.setDono(dono);
		return repo.save(animal);
	}

	
	/**
	 * Procura pelo dono no banco com o mesmo nome, se existir recupera e atualiza o email
	 * caso contrário salva um novo
	 * @param dono Dono do animal
	 * @return
	 */
	private Cliente processDono(Cliente dono) {
		Cliente cliente = clienteRepo.findOneByNomeIgnoreCase(dono.getNome()).orElse(dono);
		cliente.setEmail(dono.getEmail()); //atualiza o email
		return clienteRepo.save(cliente);
	}

	public Iterable<Animal> findAll() {
		return repo.findAll();
	}

	public Animal findOne(Long id) {
		return repo.findOne(id);
	}

	public void delete(Long id) {
		repo.delete(id);
	}


	/**
	 * <p>Adiciona a vacina ao animal de mesmo id passado como parâmetro</p>
	 * <p>Essa vacina será sempre nova ({@code id=null}) e com {@link StatusVacina} igual a {@link StatusVacina#AGENDADA}</p>
	 * @param id Id do animal
	 * @param vacina Vacina a ser adicionada
	 */
	public Vacina adicionaVacina(Long id, Vacina vacina) {
		Animal animal = findOne(id);
		
		vacina.setId(null);
		
		vacina.setStatus(StatusVacina.AGENDADA);
		vacina.setAnimal(animal);

		vacina = vacinaRepo.save(vacina);

		animal.addVacina(vacina);
		repo.save(animal);

		return vacina;
	}

	/**
	 * Exclui uma vacina
	 * @param vacinaId
	 */
	public void removeVacina(Long vacinaId) {
		Vacina vacina = vacinaRepo.findOne(vacinaId);

		Animal animal = vacina.getAnimal();
		animal.getVacinas().remove(vacina);

		vacinaRepo.delete(vacina);
		repo.save(animal);
	}


	/**
	 * Altera o status da {@link Vacina} para {@link StatusVacina#APLICADA}
	 * @param vacinaId
	 */
	public void confirmaVacina(Long vacinaId) {
		Vacina vacina = vacinaRepo.findOne(vacinaId);
		vacina.setStatus(StatusVacina.APLICADA);
		vacinaRepo.save(vacina);		
	}

}
