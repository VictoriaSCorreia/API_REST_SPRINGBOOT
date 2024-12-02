package com.project.MALocacao.exceptions;

public class QuantidadeProdutoInvalidaException extends RuntimeException {
    public QuantidadeProdutoInvalidaException() {
        super("Quantidade inválida. Informar um valor positivo.");
    }
}

