package br.com.sweetpet.builder;

import br.com.sweetpet.model.Animal;
import br.com.sweetpet.model.Cliente;
import br.com.sweetpet.model.TipoAnimal;

/**
 * Created by vinic on 28/05/2017.
 */
public class AnimalBuilder {

    private final Animal animal;

    public AnimalBuilder() {
        animal = new Animal();
        animal.setTipo(TipoAnimal.DINOSSAURO);
    }

    public AnimalBuilder nome(String nome) {
        animal.setNome(nome);
        return this;
    }

    public AnimalBuilder tipo(TipoAnimal tipo) {
        animal.setTipo(tipo);
        return this;
    }

    public AnimalBuilder dono(String nomeDoDono) {
        Cliente dono = new Cliente();
        dono.setNome(nomeDoDono);
        dono.setEmail(nomeDoDono+"@test.com");
        animal.setDono(dono);
        return this;
    }

    public Animal build(){
        return animal;
    }
}
