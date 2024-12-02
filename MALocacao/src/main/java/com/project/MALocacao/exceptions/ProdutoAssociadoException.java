package com.project.MALocacao.exceptions;

public class ProdutoAssociadoException extends RuntimeException {
    public ProdutoAssociadoException() {
        super("Não é possível excluir esta entrada pois ela está associada a um produto.");
    }
}
