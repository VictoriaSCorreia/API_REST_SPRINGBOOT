package com.project.MALocacao.exceptions;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long produtoId) {
        super("Produto não encontrado com o ID: " + produtoId);
    }
}
