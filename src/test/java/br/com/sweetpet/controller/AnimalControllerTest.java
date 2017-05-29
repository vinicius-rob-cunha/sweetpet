package br.com.sweetpet.controller;

import br.com.sweetpet.builder.AnimalBuilder;
import br.com.sweetpet.builder.ConsultaBuilder;
import br.com.sweetpet.builder.VacinaBuilder;
import br.com.sweetpet.model.Animal;
import br.com.sweetpet.model.Consulta;
import br.com.sweetpet.service.AnimalService;
import br.com.sweetpet.service.ConsultaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest
@MockBean(ConsultaService.class)
public class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @Test
    public void deveListarTodasAsConsultasNaHome() throws Exception {
        List<Animal> results = new ArrayList<>();
        results.add(new AnimalBuilder().nome("Drogon").dono("Daenerys").build());
        results.add(new AnimalBuilder().nome("Viserion").dono("Daenerys").build());
        results.add(new AnimalBuilder().nome("Rhaegal").dono("Daenerys").build());
        given(this.animalService.findAll()).willReturn(results);

        this.mockMvc.perform(get("/animais"))
                .andExpect(result -> {
                    ModelAndView mv = result.getModelAndView();
                    List<Animal> animais = (List<Animal>) mv.getModel().get("animais");
                    assertEquals(3, animais.size());
                })
                .andExpect(view().name("animais"));
    }

    @Test
    public void deveAbrirOCadastroComOsTiposCarregados() throws Exception {
        this.mockMvc.perform(get("/animais/cadastro"))
                .andExpect(model().attributeExists("tipos"))
                .andExpect(view().name("animal_cadastro"));
    }

    @Test
    public void deveCadastrarUmAnimal() throws Exception {
        this.mockMvc.perform(post("/animais")
                                    .param("nome", "Summer")
                                    .param("tipo", "CACHORRO")
                                    .param("dono.nome", "Bran")
                                    .param("dono.email", "bran@starks.com")
                            )
                            .andExpect(flash().attributeExists("success"))
                            .andExpect(redirectedUrl("/animais"));
    }

    @Test
    public void deveValidarCamposObrigatoriosEVoltarParaOCadastro() throws Exception {
        //passa o dono apenas para validar dentro do Cliente e não apenas se existe ou não o cliente
        this.mockMvc.perform(post("/animais").param("dono.nome", "")
                                                        .param("dono.email", ""))
                .andExpect(model().errorCount(4))
                .andExpect(view().name("animal_cadastro"));
    }

    @Test
    public void deveAbrirATelaDeCadastroPreenchida() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        given(this.animalService.findOne(anyLong())).willReturn(animal);

        this.mockMvc.perform(get("/animais/10"))
                .andExpect(model().attributeExists("animal"))
                .andExpect(view().name("animal_cadastro"));
    }

    @Test
    public void deveRemoverEVoltarParaALista() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        given(this.animalService.findOne(anyLong())).willReturn(animal);

        this.mockMvc.perform(get("/animais/remove/10"))
                .andExpect(flash().attributeExists("success"))
                .andExpect(redirectedUrl("/animais"));
    }

    @Test
    public void deveAbrirAListaDeVacinas() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        animal.setVacinas(VacinaBuilder.criaVacinas(10).build());
        given(this.animalService.findOne(anyLong())).willReturn(animal);

        this.mockMvc.perform(get("/animais/10/vacina"))
                .andDo(MockMvcResultHandlers.print()) //MockMvcResultHandlers.print
                .andExpect(result -> {
                    ModelAndView mv = result.getModelAndView();
                    Animal a = (Animal) mv.getModel().get("animal");
                    assertNotNull(a);
                    assertEquals(10, a.getVacinas().size());
                })
                .andExpect(view().name("vacinas"));
    }

    @Test
    public void deveCadastrarUmaVacina() throws Exception {
        this.mockMvc.perform(post("/animais/10/vacina")
                                    .param("nome", "Summer")
                                    .param("data", "20/06/2017")
                            )
                            .andExpect(flash().attributeExists("vacina_alterada"))
                            .andExpect(redirectedUrl("/animais/10/vacina"));
    }

    @Test
    public void deveValidarCamposObrigatoriosEVoltarParaAListaDeVacinas() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        animal.setVacinas(VacinaBuilder.criaVacinas(10).build());
        given(this.animalService.findOne(anyLong())).willReturn(animal);

        this.mockMvc.perform(post("/animais/10/vacina"))
                    .andExpect(model().errorCount(2))
                    .andExpect(view().name("vacinas"));
    }

    @Test
    public void deveAlterarAVacinaEVoltarParaALista() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        animal.setVacinas(VacinaBuilder.criaVacinas(10).build());
        given(this.animalService.findOne(anyLong())).willReturn(animal);

        this.mockMvc.perform(get("/animais/10/vacina/2/confirma"))
                    .andExpect(flash().attributeExists("vacina_alterada"))
                    .andExpect(redirectedUrl("/animais/10/vacina"));
    }

    @Test
    public void deveRemoverAVacinaEVoltarParaALista() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        animal.setVacinas(VacinaBuilder.criaVacinas(10).build());
        given(this.animalService.findOne(anyLong())).willReturn(animal);

        this.mockMvc.perform(get("/animais/10/vacina/2/remove"))
                    .andExpect(redirectedUrl("/animais/10/vacina"));
    }

}