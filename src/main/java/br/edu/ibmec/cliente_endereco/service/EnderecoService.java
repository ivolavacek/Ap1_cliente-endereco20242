package br.edu.ibmec.cliente_endereco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ibmec.cliente_endereco.repository.ClienteRepository;
import br.edu.ibmec.cliente_endereco.repository.EnderecoRepository;
import br.edu.ibmec.cliente_endereco.model.Endereco;
import br.edu.ibmec.cliente_endereco.exception.ClienteException;
import br.edu.ibmec.cliente_endereco.exception.EnderecoException;
import br.edu.ibmec.cliente_endereco.model.Cliente;

import java.util.Optional;
import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Endereco procurarEnderecoPorIdEClienteId(int id, int idEndereco) throws Exception {
        // Busca cliente pelo id
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (!clienteExistente.isPresent())
            throw new ClienteException("Cliente não encontrado");

        // Busca endereço pelo id
        Optional<Endereco> enderecoExistente = enderecoRepository.findById(idEndereco);

        if (!enderecoExistente.isPresent())
            throw new EnderecoException("Endereco não encontrado");

        Cliente cliente = clienteExistente.get();
        Endereco endereco = enderecoExistente.get();

        // Verifica se o endereço pertence ao cliente
        if (!cliente.getEnderecos().contains(endereco))
            throw new EnderecoException("Endereco com ID " + idEndereco + " não pertence ao cliente com ID " + id);

        return endereco;
    }

    public Endereco atualizaEndereco(int id, int idEndereco, Endereco enderecoAtualizado) throws Exception {
        // Busca cliente pelo id
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (!clienteExistente.isPresent())
            throw new ClienteException("Cliente com ID " + id + " não encontrado");

        // Busca endereço pelo id
        Optional<Endereco> enderecoExistente = enderecoRepository.findById(idEndereco);
    
        if (!enderecoExistente.isPresent()) {
            throw new EnderecoException("Endereço com ID " + idEndereco + " não encontrado");
        }
    
        Cliente cliente = clienteExistente.get();
        Endereco endereco = enderecoExistente.get();

        // Verifica se o endereço pertence ao cliente
        if (!cliente.getEnderecos().contains(endereco))
            throw new EnderecoException("Endereco com ID " + idEndereco + " não pertence ao cliente com ID " + id);
    
        // Busca todos os endereços do cliente
        List<Endereco> enderecosCliente = cliente.getEnderecos();
    
        // Verificar se o cliente já tem um endereço idêntico ao atualizado
        for (Endereco e : enderecosCliente) {
            if (e.getId() != endereco.getId() && // Exclui o endereço atual da verificação
                e.getRua().equals(enderecoAtualizado.getRua()) &&
                e.getNumero().equals(enderecoAtualizado.getNumero()) &&
                e.getComplemento().equals(enderecoAtualizado.getComplemento()) &&
                e.getCep().equals(enderecoAtualizado.getCep())) {
                    throw new EnderecoException("Endereço repetido para o mesmo cliente");
            }
        }
    
        // Atualiza os dados do endereço
        endereco.setRua(enderecoAtualizado.getRua());
        endereco.setNumero(enderecoAtualizado.getNumero());
        endereco.setComplemento(enderecoAtualizado.getComplemento());
        endereco.setBairro(enderecoAtualizado.getBairro());
        endereco.setCidade(enderecoAtualizado.getCidade());
        endereco.setEstado(enderecoAtualizado.getEstado());
        endereco.setCep(enderecoAtualizado.getCep());
    
        // Salva o endereço atualizado
        enderecoRepository.save(endereco);
    
        return endereco;
    }

    public void deletarEndereco(int id, int idEndereco) {
        // Busca cliente pelo id
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (!clienteExistente.isPresent())
            throw new ClienteException("Cliente com ID " + id + " não encontrado");
            
        // Busca endereço pelo id
        Optional<Endereco> enderecoExistente = enderecoRepository.findById(idEndereco);

        if (!enderecoExistente.isPresent())
            throw new EnderecoException("Endereco com ID " + idEndereco + " não encontrado");

        Cliente cliente = clienteExistente.get();
        Endereco endereco = enderecoExistente.get();

        // Verifica se o endereço pertence ao cliente
        if (!cliente.getEnderecos().contains(endereco))
            throw new EnderecoException("Endereco com ID " + idEndereco + " não pertence ao cliente com ID " + id);

        // Deleta o endereço
        enderecoRepository.delete(endereco);
    }
}
