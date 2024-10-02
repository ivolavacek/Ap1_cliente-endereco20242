package br.edu.ibmec.cliente_endereco.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ibmec.cliente_endereco.exception.ClienteException;
import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
public class ClienteServiceTest {

    @Autowired
    private ClienteService service;

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
    public void should_create_cliente() throws Exception {
        // Arrange
        // clientePadrao
        clientePadrao.setCpf("917.658.321-01");
        clientePadrao.setEmail("lua3@cat.com");
        clientePadrao.setTelefone("(21)12345-9905");
        // Act
        Cliente resultado = service.createCliente(clientePadrao);

        // Assert
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(resultado.getNome(), "Lua");
        Assertions.assertEquals(resultado.getCpf(), "917.658.321-01");
        Assertions.assertEquals(resultado.getEmail(), "lua@cat.com");
    }

    @Test
    public void should_not_create_cliente_menor_de_idade() throws Exception {
        // Arrange
        clientePadrao.setDataNascimento(LocalDate.parse("2020-04-20"));

        String expectedmessage = "Cliente deve ser maior de idade";

        // Act
        ClienteException exception = Assertions.assertThrows(ClienteException.class, () -> {
            throw new ClienteException("Cliente deve ser maior de idade");
        });

        // Assert
        Assertions.assertEquals(expectedmessage, exception.getMessage());
    }

    @Test
    public void should_not_create_cliente_with_same_cpf() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("Ivo");
        cliente.setEmail("ivo@ibmec.com");
        cliente.setCpf("123.456.789-09");
        cliente.setDataNascimento(LocalDate.of(1997, 1, 1));
        cliente.setTelefone("(21)12345-6799");

        String expectedmessage = "CPF já cadastrado";

        // Act
        ClienteException exception = Assertions.assertThrows(ClienteException.class, () -> {
            throw new ClienteException("CPF já cadastrado");
        });

        // Assert
        Assertions.assertEquals(expectedmessage, exception.getMessage());
    }

    @Test
    public void should_not_create_cliente_with_same_email() throws Exception {
        // Arrange
        // clientePadrao

        Cliente cliente = new Cliente();
        cliente.setNome("Lua2");
        cliente.setEmail("lua@cat.com");
        cliente.setCpf("987.654.321-00");
        cliente.setDataNascimento(LocalDate.of(2000, 2, 2));
        cliente.setTelefone("(21)12345-9955");

        String expectedmessage = "E-mail já cadastrado";

        // Act
        ClienteException exception = Assertions.assertThrows(ClienteException.class, () -> {
            throw new ClienteException("E-mail já cadastrado");
        });

        // Assert
        Assertions.assertEquals(expectedmessage, exception.getMessage());
    }

   
    @Test
    public void should_update_cliente() throws Exception {
        // Arrange
        // clientePadrao
        clientePadrao.setCpf("917.658.331-75");
        clientePadrao.setEmail("lua2@cat.com");
        clientePadrao.setTelefone("(21)12345-9902");
        Cliente cliente = service.createCliente(clientePadrao);

        String newNome = "Estrela";
        String newEmail = "estrela@cat.com";
        String newTelefone = "(21)99588-7755";

        clientePadrao.setNome(newNome);
        clientePadrao.setEmail(newEmail);
        clientePadrao.setTelefone(newTelefone);

        // Act
        Cliente resultado = service.atualizaCliente(cliente.getId(), clientePadrao);

        // Assert
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(resultado.getNome(), "Estrela");
        Assertions.assertEquals(resultado.getEmail(), "estrela@cat.com");
    }

}
