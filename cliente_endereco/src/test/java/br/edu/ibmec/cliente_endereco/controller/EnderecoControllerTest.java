package br.edu.ibmec.cliente_endereco.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;
import br.edu.ibmec.cliente_endereco.repository.ClienteRepository;
import br.edu.ibmec.cliente_endereco.service.ClienteService;
import br.edu.ibmec.cliente_endereco.service.EnderecoService;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = EnderecoController.class)
@AutoConfigureMockMvc
public class EnderecoControllerTest {

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private EnderecoController enderecoController;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private EnderecoService enderecoService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private Cliente clientePadrao;
    private Endereco enderecoPadrao;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                   .webAppContextSetup(context)
                   .build();

        // Cria um cliente padrão
        Cliente clientePadrao = new Cliente();
        clientePadrao.setId(7);
        clientePadrao.setNome("Lua");
        clientePadrao.setEmail("lua@cat.com");
        clientePadrao.setCpf("123.456.789-09");
        clientePadrao.setDataNascimento(LocalDate.of(2000, 4, 15));
        clientePadrao.setTelefone("(21)12345-9977");

        // Cria um endereço padrão
        Endereco enderecoPadrao = new Endereco();
        enderecoPadrao.setId(1);
        enderecoPadrao.setRua("Rua principal");
        enderecoPadrao.setNumero("1900");
        enderecoPadrao.setComplemento("apto 1001 bloco A");
        enderecoPadrao.setBairro("Laranjeiras");
        enderecoPadrao.setCidade("Rio de Janeiro");
        enderecoPadrao.setEstado("RJ");
        enderecoPadrao.setCep("25000-000");

        // Associa o endereço ao cliente
        clientePadrao.setEnderecos(List.of(enderecoPadrao));
    }

    @Test
    public void should_get_endereco_by_id_also_by_cliente_id() throws Exception {

        given(this.enderecoService.procurarEnderecoPorIdEClienteId(eq(7), eq(1))).willReturn(enderecoPadrao);

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.get("/cliente/7/endereco/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("Resposta JSON: " + jsonResponse);
    
        this.mvc.perform(MockMvcRequestBuilders.get("/cliente/7/endereco/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
            // .andExpect(jsonPath("$.id", is(1)))
    //         .andExpect(jsonPath("$.rua", is("Rua principal")))
    //         .andExpect(jsonPath("$.numero", is("1900")))
    //         .andExpect(jsonPath("$.complemento", is("apto 1001 bloco A")))
    //         .andExpect(jsonPath("$.bairro", is("Laranjeiras")))
    //         .andExpect(jsonPath("$.cidade", is("Rio de Janeiro")))
    //         .andExpect(jsonPath("$.estado", is("RJ"))) 
    //         .andExpect(jsonPath("$.cep", is("25000-000")));
    // }
    }
}
