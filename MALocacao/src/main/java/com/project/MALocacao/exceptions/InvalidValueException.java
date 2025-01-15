package com.project.MALocacao.exceptions;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException() {
        super("Valor deve ser maior que 0");
    }
}
