package com.project.MALocacao.exceptions;

public class ProductJaExisteException extends RuntimeException {
    public ProductJaExisteException(String nome) {
        super("O product com o nome " + nome + " já existe.");
    }
}
