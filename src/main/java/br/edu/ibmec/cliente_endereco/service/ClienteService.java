package br.edu.ibmec.cliente_endereco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ibmec.cliente_endereco.repository.ClienteRepository;
import br.edu.ibmec.cliente_endereco.repository.EnderecoRepository;
import br.edu.ibmec.cliente_endereco.exception.ClienteException;
import br.edu.ibmec.cliente_endereco.exception.EnderecoException;
import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Cliente createCliente(Cliente cliente) throws Exception {

        // Verifica se o CPF já está cadastrado
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new ClienteException("CPF já cadastrado");
        }

        // Verificar se o E-mail já está cadastrado
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new ClienteException("E-mail já cadastrado");
        }

        // Verifica se o telefone já está cadastrado
        if (clienteRepository.findByTelefone(cliente.getTelefone()).isPresent()) {
            throw new ClienteException("Telefone já cadastrado");
        }

        // Verificar se o cliente é maior de idade
        if (!eMaiorDeIdade(cliente.getDataNascimento())) {
            throw new ClienteException("Cliente deve ser maior de idade");
        }

        clienteRepository.save(cliente);

        return cliente;
    }

    public Cliente atualizaCliente(int id, Cliente clienteAtualizado) throws Exception {

        // Busca o cliente pelo id
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        // Verificar se o cliente existe
        if (!clienteExistente.isPresent()) {
            throw new ClienteException("Cliente com ID " + id + " não encontrado");
        }

        Cliente cliente = clienteExistente.get();

        // Verificar se quer trocar o e-mail
        Optional<Cliente> clienteComEmail = clienteRepository.findByEmail(clienteAtualizado.getEmail());
        if (clienteComEmail.isPresent() && clienteComEmail.get().getId() != id) {
            throw new ClienteException("E-mail já cadastrado por outro cliente");
        }

        // Verificar se quer trocar a data de nascimento
        if (!cliente.getDataNascimento().equals(clienteAtualizado.getDataNascimento())) {
            throw new ClienteException("Não é possível alterar a data de nascimento");
        }

        // Verificar se quer trocar o CPF
        if (!cliente.getCpf().equals(clienteAtualizado.getCpf())) {
            throw new ClienteException("Não é possível alterar o CPF");
        }

        // Atualiza os dados do cliente
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());

        clienteRepository.save(cliente);

        return cliente;
    }

    public Cliente associarEndereco(Endereco endereco, int id) throws Exception {
        // Busca o cliente pelo id
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (!clienteExistente.isPresent()) {
            throw new ClienteException("Cliente com ID " + id + " não encontrado");
        }

        Cliente cliente = clienteExistente.get();

        // Busca endereços do cliente
        List<Endereco> enderecos = cliente.getEnderecos();

        // Confere se o endereço é duplicado
        for (Endereco e : enderecos) {
            if (e.getRua().equals(endereco.getRua()) &&
                e.getNumero().equals(endereco.getNumero()) &&
                e.getComplemento().equals(endereco.getComplemento()) &&
                e.getCep().equals(endereco.getCep())) {
                throw new EnderecoException("Endereço já associado a este cliente");
            }
        }

        // Adiciona o endereço ao cliente
        cliente.associarEndereco(endereco);

        // Salvar endereço no repositório
        enderecoRepository.save(endereco);

        // Atualiza o cliente com novo endereço no repositório
        clienteRepository.save(cliente);

        return cliente;
    }

    public boolean eMaiorDeIdade(LocalDate dataNascimento){
        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }
}

