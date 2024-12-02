package com.project.MALocacao.exceptions;

public class ProdutoJaExisteException extends RuntimeException {
    public ProdutoJaExisteException(String nome) {
        super("O produto com o nome " + nome + " já existe.");
    }
}
