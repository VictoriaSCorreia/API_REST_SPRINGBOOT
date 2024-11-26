package com.project.MALocacao.exception;

public class QuantidadeProdutoInvalidaException extends RuntimeException {
    public QuantidadeProdutoInvalidaException() {
        super("Quantidade inválida. Informar um valor positivo.");

    }
}

