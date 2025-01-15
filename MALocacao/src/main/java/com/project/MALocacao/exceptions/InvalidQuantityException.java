package com.project.MALocacao.exceptions;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super("Quantidade inválida. Informar um valor positivo ou que não ultrapasse o estoque.");
    }
}
