package br.com.sweetpet.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.sweetpet.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

	Optional<Cliente> findOneByNomeIgnoreCase(String nome);	
	
}
