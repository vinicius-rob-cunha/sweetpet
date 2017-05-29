package br.com.sweetpet.controller;

import br.com.sweetpet.builder.AnimalBuilder;
import br.com.sweetpet.builder.ConsultaBuilder;
import br.com.sweetpet.model.Animal;
import br.com.sweetpet.model.Consulta;
import br.com.sweetpet.service.AnimalService;
import br.com.sweetpet.service.ConsultaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService service;

    @MockBean
    private AnimalService animalService;

    @Test
    public void deveListarTodasAsConsultasNaHome() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        given(this.service.findAll()).willReturn(ConsultaBuilder.cria(3)
                                                                .veterinario("Dr. Dolittle")
                                                                .animal(animal)
                                                                .build());

        this.mockMvc.perform(get("/consultas"))
                    .andExpect(result -> {
                        ModelAndView mv = result.getModelAndView();
                        List<Consulta> consultas = (List<Consulta>) mv.getModel().get("consultas");
                        assertEquals(3, consultas.size());
                    })
                    .andExpect(view().name("consultas"));
    }

    @Test
    public void deveAbrirOCadastroComOsAnimaisCarregados() throws Exception {
        List<Animal> results = new ArrayList<>();
        results.add(new AnimalBuilder().nome("Drogon").dono("Daenerys").build());
        results.add(new AnimalBuilder().nome("Viserion").dono("Daenerys").build());
        results.add(new AnimalBuilder().nome("Rhaegal").dono("Daenerys").build());
        given(this.animalService.findAll()).willReturn(results);

        this.mockMvc.perform(get("/consultas/cadastro"))
                .andExpect(result -> {
                    ModelAndView mv = result.getModelAndView();
                    List<Animal> animais = (List<Animal>) mv.getModel().get("animais");
                    assertEquals(3, animais.size());
                })
                .andExpect(view().name("consulta_cadastro"));
    }

    @Test
    public void deveCadastrarUmaConsulta() throws Exception {
        this.mockMvc.perform(post("/consultas")
                                .param("veterinario.nome", "Dr. Dolittle")
                                .param("animal.id", "2")
                                .param("hora", "11")
                                .param("minuto", "30")
                                .param("valor", "120")
                                .param("data", "28/05/2017")
                                .param("detalhes", "Detalhes não são necessarios")
                             )
                .andExpect(flash().attributeExists("success"))
                .andExpect(redirectedUrl("/consultas"));
    }

    @Test
    public void deveValidarCamposObrigatoriosEVoltarParaOCadastro() throws Exception {
        this.mockMvc.perform(post("/consultas"))
                .andExpect(model().errorCount(7))
                .andExpect(view().name("consulta_cadastro"));
    }

    @Test
    public void deveAbrirATelaDeCadastroPreenchida() throws Exception {
        Animal animal = new AnimalBuilder().nome("Drogon").dono("Daenerys").build();
        given(this.service.findOne(anyLong())).willReturn(ConsultaBuilder.cria()
                                                                         .veterinario("Dr. Dolittle")
                                                                         .animal(animal)
                                                                         .buildOne());

        this.mockMvc.perform(get("/consultas/10"))
                .andExpect(model().attributeExists("consulta"))
                .andExpect(view().name("consulta_cadastro"));
    }



}