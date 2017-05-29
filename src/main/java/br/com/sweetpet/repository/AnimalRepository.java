package br.com.sweetpet.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.sweetpet.model.Animal;

public interface AnimalRepository extends CrudRepository<Animal, Long> {
}
