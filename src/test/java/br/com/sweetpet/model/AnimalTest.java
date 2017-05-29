package br.com.sweetpet.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static br.com.sweetpet.builder.VacinaBuilder.criaVacinas;
import static br.com.sweetpet.model.StatusVacina.*;
import static org.junit.Assert.*;

/**
 * Testa os metodos de controle de vacina da entidade {@link Animal}
 */
public class AnimalTest {

    private Animal animal;

    @Before
    public void prepare(){
        animal = new Animal();
        animal.setVacinas(
            criaVacinas(7)
                .comDataEStatus(LocalDate.of(2017, Month.JUNE, 12),AGENDADA,0)
                .comDataEStatus(LocalDate.of(2017, Month.JUNE, 10),APLICADA,1)
                .comDataEStatus(LocalDate.of(2017, Month.JUNE, 20),AGENDADA,2)
                .comDataEStatus(LocalDate.of(2017, Month.JUNE,  3),APLICADA,3)
                .comDataEStatus(LocalDate.of(2017, Month.JUNE,  5),AGENDADA,4)
                .comDataEStatus(LocalDate.of(2017, Month.JUNE, 28),APLICADA,5)
                .comDataEStatus(LocalDate.of(2017, Month.JUNE, 18),AGENDADA,6)
                .build()
        );
    }

    @Test
    public void deveRetornarApenasAVacinaComADataMaisProximaComStatusAgendado(){
        Vacina vacina = animal.getProximaVacina();
        assertEquals(LocalDate.of(2017, Month.JUNE, 5), vacina.getData());
    }

    @Test
    public void deveRetornarAsVacinasOrdenadasPorDataCrescente(){
        List<Vacina> vacinas = animal.getVacinasOrdenadas();
        assertEquals( 3, vacinas.get(0).getData().getDayOfMonth());
        assertEquals( 5, vacinas.get(1).getData().getDayOfMonth());
        assertEquals(10, vacinas.get(2).getData().getDayOfMonth());
        assertEquals(12, vacinas.get(3).getData().getDayOfMonth());
        assertEquals(18, vacinas.get(4).getData().getDayOfMonth());
        assertEquals(20, vacinas.get(5).getData().getDayOfMonth());
        assertEquals(28, vacinas.get(6).getData().getDayOfMonth());
    }

}