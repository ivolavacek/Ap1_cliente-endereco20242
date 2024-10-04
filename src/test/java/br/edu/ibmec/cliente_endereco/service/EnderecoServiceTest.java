package br.edu.ibmec.cliente_endereco.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;

@SpringBootTest
@ActiveProfiles("test")
public class EnderecoServiceTest {

    @Autowired
    private EnderecoService service;

    @Autowired
    private ClienteService clienteService;
    
    private Cliente clientePadrao;
    private Endereco enderecoPadrao;

    @BeforeEach
    public void setup() {
        // Cria um cliente padrão
        clientePadrao = new Cliente();
        clientePadrao.setNome("Lua");
        clientePadrao.setCpf("123.456.789-09");
        clientePadrao.setEmail("lua@cat.com");
        clientePadrao.setTelefone("(21)99888-0000");
        clientePadrao.setDataNascimento(LocalDate.parse("2000-04-20"));

        // Cria um endereço padrão
        enderecoPadrao = new Endereco();
        enderecoPadrao.setRua("Rua Whiskas");
        enderecoPadrao.setNumero("123");
        enderecoPadrao.setComplemento("Casa 3");
        enderecoPadrao.setBairro("Botafogo");
        enderecoPadrao.setCidade("Rio de Janeiro");
        enderecoPadrao.setEstado("RJ");
        enderecoPadrao.setCep("22222-000");
    }

    @Test
    public void should_update_endereco() throws Exception {
        // Arrange
        Cliente cliente = clienteService.createCliente(clientePadrao);
        Cliente clienteComEndereco = clienteService.associarEndereco(enderecoPadrao, cliente.getId());
        List<Endereco> enderecos = clienteComEndereco.getEnderecos();
        Endereco endereco = enderecos.get(0);
        
        enderecoPadrao.setBairro("Laranjeiras");
        enderecoPadrao.setCep("22222-233");

        // Act
        Endereco resultado = service.atualizaEndereco(cliente.getId(), endereco.getId(), enderecoPadrao);

        // Assert
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(resultado.getBairro(), "Laranjeiras");
        Assertions.assertEquals(resultado.getCep(), "22222-233");

    }
}
