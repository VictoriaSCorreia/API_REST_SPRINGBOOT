package com.project.MALocacao.exception;

public class QuantidadeInvalidaException extends RuntimeException {
    public QuantidadeInvalidaException() {
        super("Quantidade informada inválida. Deve ser um valor positivo.");

    }
}
