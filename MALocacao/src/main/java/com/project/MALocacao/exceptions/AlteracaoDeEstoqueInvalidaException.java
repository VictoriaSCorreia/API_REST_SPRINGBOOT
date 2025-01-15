package com.project.MALocacao.exceptions;

public class AlteracaoDeEstoqueInvalidaException extends RuntimeException {
    public AlteracaoDeEstoqueInvalidaException() {
        super("O número de unidades em estoque só pode ser alterado através da Inbound e Saída.");
    }
}

