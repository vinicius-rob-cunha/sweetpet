package br.com.sweetpet.repository;

import br.com.sweetpet.model.Cliente;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Teste para a busca de clientes
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ClienteRepositoryTest {

    private static final String JOAO = "João";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository repository;

    @Before
    public void prepareData() throws Exception {
        criaCliente(JOAO);
    }

    @Test
    public void deveEncontrarOJoaoComIndependenteDoCaseDasLetras() throws Exception {
        assertEquals(JOAO, repository.findOneByNomeIgnoreCase("joão").get().getNome());
        assertEquals(JOAO, repository.findOneByNomeIgnoreCase("JOÃO").get().getNome());
        assertEquals(JOAO, repository.findOneByNomeIgnoreCase("JoÃo").get().getNome());
        assertEquals(JOAO, repository.findOneByNomeIgnoreCase("joÃo").get().getNome());
        assertEquals(JOAO, repository.findOneByNomeIgnoreCase("joãO").get().getNome());
    }

    @Test
    public void naoDeveEncontrarOJoao() throws Exception {
        assertFalse(repository.findOneByNomeIgnoreCase("joã").isPresent());
        assertFalse(repository.findOneByNomeIgnoreCase("Jo").isPresent());
        assertFalse(repository.findOneByNomeIgnoreCase("Joao").isPresent());
    }

    private void criaCliente(String nome) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(nome + "@test.com");
        entityManager.persist(cliente);
    }

}