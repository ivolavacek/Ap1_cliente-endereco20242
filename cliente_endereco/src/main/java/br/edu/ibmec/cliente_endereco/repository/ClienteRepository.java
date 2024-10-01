package br.edu.ibmec.cliente_endereco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.cliente_endereco.model.Cliente;
import br.edu.ibmec.cliente_endereco.model.Endereco;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByCpf(String cpf);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByTelefone(String telefone);

    Optional<Cliente> findByEnderecos(Endereco endereco);
}
