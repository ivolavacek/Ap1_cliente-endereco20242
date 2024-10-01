package br.edu.ibmec.cliente_endereco.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;
import br.edu.ibmec.cliente_endereco.repository.ClienteRepository;
import br.edu.ibmec.cliente_endereco.service.ClienteService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                   .webAppContextSetup(context)
                   .build();
    }

    @Test
    public void should_create_cliente() throws Exception {
        
        Cliente cliente = new Cliente();
        cliente.setId(7);
        cliente.setNome("Lua");
        cliente.setEmail("lua@cat.com");
        cliente.setCpf("123.456.789-09");
        cliente.setDataNascimento(LocalDate.of(2020, 4, 15));
        cliente.setTelefone("(21)12345-9977");


        given(this.clienteService.createCliente(any(Cliente.class))).willReturn(cliente);


        this.mvc.perform(post("/cliente")
            .content(this.mapper.writeValueAsString(cliente))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())  
            .andExpect(jsonPath("$.id", is(7)))
            .andExpect(jsonPath("$.nome", is("Lua")))
            .andExpect(jsonPath("$.cpf", is("123.456.789-09")))
            .andExpect(jsonPath("$.email", is("lua@cat.com")))
            .andExpect(jsonPath("$.telefone", is("(21)12345-9977")));
    }

    @Test
    public void should_get_cliente() throws Exception {
       
        Cliente cliente = new Cliente();
        cliente.setId(3);
        cliente.setNome("Estrela");
        cliente.setEmail("estrela@cat.com");


        given(this.clienteRepository.findById(3)).willReturn(Optional.of(cliente));


        this.mvc.perform(MockMvcRequestBuilders.get("/cliente/3")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome", is("Estrela")));
    }

    @Test
    public void should_return_bad_request_when_cliente_is_invalid() throws Exception {

        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setId(1);
        clienteInvalido.setNome("A");
        clienteInvalido.setEmail("@");
        clienteInvalido.setCpf("123.456.654-55");
        clienteInvalido.setDataNascimento(LocalDate.of(2024, 12, 31));
        clienteInvalido.setTelefone("21993456789");


        given(this.clienteRepository.save(any(Cliente.class))).willReturn(clienteInvalido);


        this.mvc.perform(MockMvcRequestBuilders.post("/cliente")
            .content(this.mapper.writeValueAsString(clienteInvalido))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())  // Espera o erro 400
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field", Matchers.hasItems("cpf", "dataNascimento", "telefone", "email", "nome")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message", Matchers.hasItems(
                "CPF inválido", 
                "Data de nascimento inválida", 
                "O telefone deve estar no formato (XX)XXXXX-XXXX", 
                "E-mail inválido", 
                "O nome deve ter entre 3 e 100 caracteres."
        )));
    }


    @Test
    public void should_create_endereco() throws Exception {
        
        Cliente cliente = new Cliente();
        cliente.setId(7);
        cliente.setNome("Lua");
        cliente.setEmail("lua@cat.com");
        cliente.setCpf("123.456.789-09");
        cliente.setDataNascimento(LocalDate.of(2000, 4, 15));
        cliente.setTelefone("(21)12345-9977");

        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setRua("Rua principal");
        endereco.setNumero("1900");
        endereco.setComplemento("apto 1001 bloco A");
        endereco.setBairro("Laranjeiras");
        endereco.setCidade("Rio de Janeiro");
        endereco.setEstado("RJ");
        endereco.setCep("25000-000");

        // Associar o endereço ao cliente
        cliente.setEnderecos(List.of(endereco));

        given(this.clienteService.createCliente(any(Cliente.class))).willReturn(cliente);
        given(this.clienteService.associarEndereco(any(Endereco.class),eq(7))).willReturn(cliente);

        this.mvc.perform(MockMvcRequestBuilders.post("/cliente/7/endereco")
            .content(this.mapper.writeValueAsString(endereco))  // Enviando o endereço para associação
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())  // Espera o status 201 Created
            .andExpect(jsonPath("$.id", is(7)))  // Verifica o id do cliente
            .andExpect(jsonPath("$.nome", is("Lua")))  // Verifica o nome do cliente
            .andExpect(jsonPath("$.cpf", is("123.456.789-09")))  // Verifica o CPF do cliente
            .andExpect(jsonPath("$.email", is("lua@cat.com")))  // Verifica o email do cliente
            .andExpect(jsonPath("$.telefone", is("(21)12345-9977")))  // Verifica o telefone do cliente
            .andExpect(jsonPath("$.enderecos[0].id", is(1)))  // Verifica o id do endereço associado
            .andExpect(jsonPath("$.enderecos[0].rua", is("Rua principal")))  // Verifica a rua do endereço
            .andExpect(jsonPath("$.enderecos[0].numero", is("1900")))  // Verifica o número do endereço
            .andExpect(jsonPath("$.enderecos[0].complemento", is("apto 1001 bloco A")))  // Verifica o complemento
            .andExpect(jsonPath("$.enderecos[0].bairro", is("Laranjeiras")))  // Verifica o bairro do endereço
            .andExpect(jsonPath("$.enderecos[0].cidade", is("Rio de Janeiro")))  // Verifica a cidade do endereço
            .andExpect(jsonPath("$.enderecos[0].estado", is("RJ")))  // Verifica o estado do endereço
            .andExpect(jsonPath("$.enderecos[0].cep", is("25000-000")));  // Verifica o CEP do endereço
    }

}
