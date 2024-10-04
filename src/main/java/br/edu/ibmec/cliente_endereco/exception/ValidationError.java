package br.edu.ibmec.cliente_endereco.exception;

import lombok.Data;

@Data
public class ValidationError {
    private String field;
    private String message;
}
