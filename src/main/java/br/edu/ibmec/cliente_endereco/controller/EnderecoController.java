package br.edu.ibmec.cliente_endereco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import br.edu.ibmec.cliente_endereco.repository.ClienteRepository;
import br.edu.ibmec.cliente_endereco.service.EnderecoService;
import br.edu.ibmec.cliente_endereco.exception.ClienteException;
import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cliente/{id}/endereco")
public class EnderecoController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoService service;

    @GetMapping
    public ResponseEntity<List<Endereco>> getEndereco(@PathVariable("id") int id) throws Exception {
        Optional<Cliente> tryCliente = clienteRepository.findById(id);

        if (!tryCliente.isPresent())
            throw new ClienteException("Cliente com ID " + id + "não encontrado");

        Cliente cliente = tryCliente.get();

        List<Endereco> Enderecos = cliente.getEnderecos();

        return new ResponseEntity<>(Enderecos, HttpStatus.OK);
    }

    @GetMapping("{idEndereco}")
    public ResponseEntity<Endereco> getEnderecoById(@PathVariable("id") int id, @PathVariable("idEndereco") int idEndereco) throws Exception {
        Endereco response = service.procurarEnderecoPorIdEClienteId(id, idEndereco);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{idEndereco}")
    public ResponseEntity<Endereco> updateEndereco(@PathVariable("id") int id, @PathVariable("idEndereco") int idEndereco, @Valid @RequestBody Endereco enderecoAtualizado) throws Exception {
        Endereco response = service.atualizaEndereco(id, idEndereco, enderecoAtualizado);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{idEndereco}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable("id") int id, @PathVariable("idEndereco") int idEndereco) throws Exception{
        service.deletarEndereco(id, idEndereco);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
