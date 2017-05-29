package br.com.sweetpet.repository;

import br.com.sweetpet.model.Cliente;
import br.com.sweetpet.model.Veterinario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Teste para a busca de clientes
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VeterinarioRepositoryTest {

    private static final String MICHEL = "Michel";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VeterinarioRepository repository;

    @Before
    public void prepareData() throws Exception {
        criaVeterinario(MICHEL);
    }

    @Test
    public void deveEncontrarOMichelComIndependenteDoCaseDasLetras() throws Exception {
        assertEquals(MICHEL, repository.findOneByNomeIgnoreCase("michel").get().getNome());
        assertEquals(MICHEL, repository.findOneByNomeIgnoreCase("MICHEL").get().getNome());
        assertEquals(MICHEL, repository.findOneByNomeIgnoreCase("MiCheL").get().getNome());
        assertEquals(MICHEL, repository.findOneByNomeIgnoreCase("mIchEL").get().getNome());
        assertEquals(MICHEL, repository.findOneByNomeIgnoreCase("miCHel").get().getNome());
    }

    @Test
    public void naoDeveEncontrarOMichel() throws Exception {
        assertFalse(repository.findOneByNomeIgnoreCase("miche").isPresent());
        assertFalse(repository.findOneByNomeIgnoreCase("michelle").isPresent());
        assertFalse(repository.findOneByNomeIgnoreCase("mi").isPresent());
        assertFalse(repository.findOneByNomeIgnoreCase("michell").isPresent());
    }

    private void criaVeterinario(String nome) {
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(nome);
        entityManager.persist(veterinario);
    }

}