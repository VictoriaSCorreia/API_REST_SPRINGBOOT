package com.project.MALocacao.exception;

public class ProdutoJaExisteException extends RuntimeException {
    public ProdutoJaExisteException(String nome) {
        super("O produto com o nome " + nome + " já existe.");
    }
}
