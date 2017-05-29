package br.com.sweetpet.repository;

import br.com.sweetpet.model.Vacina;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import static br.com.sweetpet.builder.VacinaBuilder.criaVacinas;
import static br.com.sweetpet.model.StatusVacina.AGENDADA;
import static br.com.sweetpet.model.StatusVacina.APLICADA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Teste para a busca de clientes
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VacinaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VacinaRepository repository;

    @Before
    public void prepareData() {
        criaVacinas(8)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE,  3),APLICADA,0)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE,  5),APLICADA,1)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE, 11),APLICADA,2)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE, 12),AGENDADA,3)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE, 18),AGENDADA,4)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE, 20),AGENDADA,5)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE, 21),APLICADA,6)
            .comDataEStatus(LocalDate.of(2017, Month.JUNE, 28),APLICADA,7)
            .build()
            .forEach(entityManager::persist);
    }

    @Test
    public void deveEncontrarSomenteVacinasAsAgendadas() {
        Collection<Vacina> vacinas = repository.findByStatusAndDataBetween(
                AGENDADA, LocalDate.of(2017, Month.JUNE, 1),
                          LocalDate.of(2017, Month.JUNE, 28)
        );

        assertEquals(3, vacinas.size());
    }

    @Test
    public void deveEncontrarSomenteVacinasAsAplicadas() {
        Collection<Vacina> vacinas = repository.findByStatusAndDataBetween(
                APLICADA, LocalDate.of(2017, Month.JUNE, 1),
                          LocalDate.of(2017, Month.JUNE, 28)
        );

        assertEquals(5, vacinas.size());
    }

    @Test
    public void deveEncontrarSomenteVacinasAsAgendadasComDatasExatas() {
        Collection<Vacina> vacinas = repository.findByStatusAndDataBetween(
                AGENDADA, LocalDate.of(2017, Month.JUNE, 12),
                          LocalDate.of(2017, Month.JUNE, 20)
        );

        assertEquals(3, vacinas.size());
    }

    @Test
    public void naoDeveEncontrarNenhumaAplicada() {
        Collection<Vacina> vacinas = repository.findByStatusAndDataBetween(
                APLICADA, LocalDate.of(2017, Month.JUNE, 12),
                          LocalDate.of(2017, Month.JUNE, 20)
        );

        assertTrue(vacinas.isEmpty());
    }

}