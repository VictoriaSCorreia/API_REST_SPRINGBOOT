package com.project.MALocacao.exception;

public class AlteracaoDeEstoqueInvalidaException extends RuntimeException {
    public AlteracaoDeEstoqueInvalidaException() {
        super("O número de unidades em estoque só pode ser alterado através da Entrada e Saída.");
    }
}

