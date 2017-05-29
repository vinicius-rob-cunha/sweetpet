package br.com.sweetpet.builder;

import br.com.sweetpet.model.StatusVacina;
import br.com.sweetpet.model.Vacina;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by vinic on 28/05/2017.
 */
public class VacinaBuilder {

    private List<Vacina> vacinas;

    private VacinaBuilder(List<Vacina> vacinas) {
        this.vacinas = vacinas;
    }

    /**
     * Cria x vacinas sem data
     * @param count número de vacinas
     * @return VacinaBuilder oara contuniar setando as datas
     */
    public static VacinaBuilder criaVacinas(int count) {
        List<Vacina> vacinas = IntStream.rangeClosed(1, count)
                                        .mapToObj(VacinaBuilder::createVacina)
                                        .collect(Collectors.toList());
        return new VacinaBuilder(vacinas);
    }

    public VacinaBuilder comDataEStatus(LocalDate data, StatusVacina status, int index){
        Vacina vacina = vacinas.get(index);
        vacina.setData(data);
        vacina.setStatus(status);
        return this;
    }

    public List<Vacina> build(){
        return vacinas;
    }

    private static Vacina createVacina(int num) {
        Vacina vacina = new Vacina();
        vacina.setNome("Vacina " + num);
        vacina.setStatus(StatusVacina.AGENDADA);
        vacina.setData(LocalDate.now());
        return vacina;
    }

    public Vacina buildOne() {
        return vacinas.get(0);
    }
}
