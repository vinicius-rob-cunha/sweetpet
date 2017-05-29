package br.com.sweetpet.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.sweetpet.model.Veterinario;

public interface VeterinarioRepository extends CrudRepository<Veterinario, Long> {

	Optional<Veterinario> findOneByNomeIgnoreCase(String nome);	
	
}
