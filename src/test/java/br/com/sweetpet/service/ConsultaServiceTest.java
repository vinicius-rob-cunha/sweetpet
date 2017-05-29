package br.com.sweetpet.service;

import br.com.sweetpet.builder.AnimalBuilder;
import br.com.sweetpet.builder.ConsultaBuilder;
import br.com.sweetpet.model.Animal;
import br.com.sweetpet.model.Consulta;
import br.com.sweetpet.model.StatusVacina;
import br.com.sweetpet.model.Vacina;
import br.com.sweetpet.repository.*;
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
public class ConsultaServiceTest {

    @Autowired
    private ConsultaService service;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private ConsultaRepository consultaRepo;

    @Autowired
    private VeterinarioRepository veterinarioRepo;

    private Consulta consulta;
    private Animal animal;

    @Before
    public void prepare(){
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        this.animal = animalService.save(animal);
        Consulta consulta = ConsultaBuilder.cria().veterinario("Dr. Dolittle").animal(animal).buildOne();
        this.consulta = service.save(consulta);
    }

    @Test
    public void deveSalvarODonoApenasUmaVez(){
        assertEquals(1, veterinarioRepo.count());

        consulta = ConsultaBuilder.cria().veterinario("Dr. Dolittle").animal(animal).buildOne();
        service.save(consulta);

        assertEquals(1, veterinarioRepo.count());

        consulta = ConsultaBuilder.cria().veterinario("Maya Dolittle").animal(animal).buildOne();
        service.save(consulta);

        assertEquals(2, veterinarioRepo.count());
        assertEquals(3, consultaRepo.count());
    }

    @TestConfiguration
    static class ConsultaServiceTestConfiguration {
        @Bean
        public ConsultaService consultaService() {
            return new ConsultaService();
        }
        @Bean
        public AnimalService animalService() {
            return new AnimalService();
        }
    }

}