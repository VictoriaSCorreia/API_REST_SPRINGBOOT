package com.project.MALocacao.exceptions;

public class InvalidProductQuantityException extends RuntimeException {
    public InvalidProductQuantityException() {
        super("Quantidade inválida. Informar um valor positivo.");
    }
}

