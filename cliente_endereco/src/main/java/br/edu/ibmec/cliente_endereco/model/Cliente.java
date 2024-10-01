package br.edu.ibmec.cliente_endereco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "Nome do cliente é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s'-]+$", message = "O nome deve conter apenas letras e espaços.")
    private String nome;

    @Column
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @Column
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @Column
    @NotNull(message = "Data de nascimento é obrigatório")
    @Past(message = "Data de nascimento inválida")
    private LocalDate dataNascimento;

    @Column
    @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX)XXXXX-XXXX")
    private String telefone;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id", name = "cliente_id")
    private List<Endereco> enderecos = new ArrayList<>();

    public void associarEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
    }
}
