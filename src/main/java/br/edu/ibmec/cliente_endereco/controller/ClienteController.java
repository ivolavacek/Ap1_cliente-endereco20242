package br.edu.ibmec.cliente_endereco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import br.edu.ibmec.cliente_endereco.repository.ClienteRepository;
import br.edu.ibmec.cliente_endereco.service.ClienteService;
import br.edu.ibmec.cliente_endereco.exception.ClienteException;
import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService service;

    @GetMapping
    public ResponseEntity<List<Cliente>> getCliente() {
        List<Cliente> Clientes = clienteRepository.findAll();
        return new ResponseEntity<>(Clientes, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable("id") int id) throws Exception {
        Optional<Cliente> tryResponse = clienteRepository.findById(id);

        if (!tryResponse.isPresent())
            throw new ClienteException("Cliente com ID " + id + " não encontrado");

        Cliente response = tryResponse.get();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cliente> saveCliente(@Valid @RequestBody Cliente cliente) throws Exception {
        Cliente response = service.createCliente(cliente);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable("id") int id, @Valid @RequestBody Cliente clienteAtualizado) throws Exception {
        Cliente response = service.atualizaCliente(id, clienteAtualizado);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("{id}/endereco")
    public ResponseEntity<Cliente> associarEnderecoAoCliente(@PathVariable("id") int id, @Valid @RequestBody Endereco endereco) throws Exception {
        Cliente response = service.associarEndereco(endereco, id);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
