package br.com.sweetpet.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.sweetpet.model.Consulta;

public interface ConsultaRepository extends CrudRepository<Consulta, Long> {
}
