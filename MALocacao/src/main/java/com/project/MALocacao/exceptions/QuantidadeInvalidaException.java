package com.project.MALocacao.exceptions;

public class QuantidadeInvalidaException extends RuntimeException {
    public QuantidadeInvalidaException() {
        super("Quantidade inválida. Informar um valor positivo ou que não ultrapasse o estoque.");
    }
}
