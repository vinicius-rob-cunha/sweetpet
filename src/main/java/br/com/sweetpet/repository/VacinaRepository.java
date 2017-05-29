package br.com.sweetpet.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import br.com.sweetpet.model.StatusVacina;
import br.com.sweetpet.model.Vacina;

public interface VacinaRepository extends CrudRepository<Vacina, Long> {
	
	Collection<Vacina> findByStatusAndDataBetween(StatusVacina status, LocalDate dataInicial, LocalDate dataFinal);

}
