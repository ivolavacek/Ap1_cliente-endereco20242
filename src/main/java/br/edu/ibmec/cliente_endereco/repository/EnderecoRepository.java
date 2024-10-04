package br.edu.ibmec.cliente_endereco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.cliente_endereco.model.Endereco;


@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

}
