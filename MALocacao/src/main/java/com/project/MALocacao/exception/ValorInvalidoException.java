package com.project.MALocacao.exception;

public class ValorInvalidoException extends RuntimeException {
    public ValorInvalidoException() {
        super("Valor deve ser maior que 0");
    }
}