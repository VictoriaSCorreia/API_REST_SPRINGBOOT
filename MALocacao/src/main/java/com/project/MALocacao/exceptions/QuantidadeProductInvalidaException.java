package com.project.MALocacao.exceptions;

public class QuantidadeProductInvalidaException extends RuntimeException {
    public QuantidadeProductInvalidaException() {
        super("Quantidade inválida. Informar um valor positivo.");
    }
}

