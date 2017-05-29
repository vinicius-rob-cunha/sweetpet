package br.com.sweetpet.builder;

import br.com.sweetpet.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by vinic on 28/05/2017.
 */
public class ConsultaBuilder {

    private final List<Consulta> consultas;

    private ConsultaBuilder(List<Consulta> consultas){
        this.consultas = consultas;
    }

    public static ConsultaBuilder cria() {
        return cria(1);
    }

    public static ConsultaBuilder cria(int count) {
        return new ConsultaBuilder(
            IntStream.rangeClosed(1, count)
                     .mapToObj(i->criaConsulta())
                     .collect(Collectors.toList())
        );
    }

    private static Consulta criaConsulta(){
        Consulta consulta = new Consulta();
        consulta.setData(LocalDate.now());
        consulta.setDetalhes("Dragão soltava gelo ao invés de fogo");
        consulta.setHora(11);
        consulta.setMinuto(30);
        consulta.setValor(BigDecimal.TEN);
        return consulta;
    }

    public Consulta buildOne(){
        return consultas.get(0);
    }

    public ConsultaBuilder veterinario(String nomeDoVeterinario) {
        consultas.forEach(consulta -> {
            Veterinario veterinario = new Veterinario();
            veterinario.setNome(nomeDoVeterinario);
            consulta.setVeterinario(veterinario);
        });
        return this;
    }

    public ConsultaBuilder animal(Animal animal) {
        consultas.forEach(consulta -> consulta.setAnimal(animal));
        return this;
    }


    public List<Consulta> build() {
        return consultas;
    }
}
