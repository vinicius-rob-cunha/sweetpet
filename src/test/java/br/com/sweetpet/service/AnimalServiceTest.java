package br.com.sweetpet.service;

import br.com.sweetpet.builder.AnimalBuilder;
import br.com.sweetpet.model.Animal;
import br.com.sweetpet.model.StatusVacina;
import br.com.sweetpet.model.Vacina;
import br.com.sweetpet.repository.AnimalRepository;
import br.com.sweetpet.repository.ClienteRepository;
import br.com.sweetpet.repository.VacinaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static br.com.sweetpet.builder.VacinaBuilder.criaVacinas;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AnimalServiceTest {

    @Autowired
    private AnimalService service;

    @Autowired
    private AnimalRepository animalRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private VacinaRepository vacinaRepo;

    private Animal animal;

    @Before
    public void prepare(){
        Animal animal = new AnimalBuilder().nome("Summer").dono("Bran").build();
        this.animal = service.save(animal);
    }

    @Test
    public void deveSalvarODonoApenasUmaVez(){
        assertEquals(1, clienteRepo.count());

        animal = new AnimalBuilder().nome("Grey Wind").dono("Bran").build();
        service.save(animal);

        assertEquals(1, clienteRepo.count());

        animal = new AnimalBuilder().nome("Ghost").dono("Jon Snow").build();
        service.save(animal);

        assertEquals(2, clienteRepo.count());
        assertEquals(3, animalRepo.count());
    }

    @Test
    public void deveAdicionarSempreUmaVacinaComStatusAgendada(){
        Vacina vacina = criaVacinas(1).buildOne();
        vacina.setStatus(StatusVacina.APLICADA);
        service.adicionaVacina(animal.getId(), vacina);

        assertEquals(StatusVacina.AGENDADA, vacinaRepo.findOne(1L).getStatus());
    }

    @Test
    public void deveAdicionarSempreUmaNovaVacina(){
        Vacina vacina = criaVacinas(1).buildOne();
        service.adicionaVacina(animal.getId(), vacina);

        assertEquals(1, vacinaRepo.count());

        vacina = criaVacinas(1).buildOne();
        vacina.setId(1L);
        service.adicionaVacina(animal.getId(), vacina);

        assertEquals(2, vacinaRepo.count());
        assertEquals(2, animal.getVacinas().size());
    }

    @Test
    public void deveExcluirUmaVacina(){
        List<Vacina> vacinas = criaVacinas(3).build().stream()
                                             .map(v -> service.adicionaVacina(animal.getId(), v))
                                             .collect(Collectors.toList());

        assertEquals(3, vacinaRepo.count());
        assertEquals(3, animal.getVacinas().size());

        service.removeVacina(vacinas.get(1).getId());

        assertEquals(2, vacinaRepo.count());
        assertEquals(2, animal.getVacinas().size());
    }

    @Test
    public void deveAlterarOStatusDaVacinaParaAplicada(){
        criaVacinas(3).build()
                      .forEach(v -> service.adicionaVacina(animal.getId(), v));

        long count = StreamSupport.stream(vacinaRepo.findAll().spliterator(), false)
                                  .filter(vacina -> vacina.getStatus() == StatusVacina.APLICADA)
                                  .count();

        assertEquals(0, count);

        service.confirmaVacina(2L);

        count = StreamSupport.stream(vacinaRepo.findAll().spliterator(), false)
                             .filter(vacina -> vacina.getStatus() == StatusVacina.APLICADA)
                             .count();

        assertEquals(1, count);
    }

    @TestConfiguration
    static class AnimalServiceTestConfiguration {
        @Bean
        public AnimalService animalService() {
            return new AnimalService();
        }
    }

}