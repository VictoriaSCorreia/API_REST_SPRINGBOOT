package com.project.MALocacao.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String nome) {
        super("O product com o nome " + nome + " já existe.");
    }
}
