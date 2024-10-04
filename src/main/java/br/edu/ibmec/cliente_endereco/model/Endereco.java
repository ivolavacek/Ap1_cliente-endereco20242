package br.edu.ibmec.cliente_endereco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "Rua é obrigatório")
    @Size(min = 3, max = 255, message = "A rua deve ter entre 3 e 255 caracteres.")
    private String rua;

    @Column
    @NotBlank(message = "Número é obrigatório")
    private String numero;

    @Column
    private String complemento;

    @Column
    @NotBlank(message = "Bairro é obrigatório")
    @Size(min = 3, max = 100, message = "O bairro deve ter entre 3 e 100 caracteres.")
    private String bairro;

    @Column
    @NotBlank(message = "Cidade é obrigatório")
    @Size(min = 2, max = 100, message = "A cidade deve ter entre 2 e 100 caracteres.")
    private String cidade;

    @Column
    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "^(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)$", 
             message = "O estado deve ser uma sigla válida.")
    private String estado;

    @Column
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}\\-\\d{3}", message = "O CEP deve estar no formato XXXXX-XXX")
    private String cep;  // Como validar o CEP?
}