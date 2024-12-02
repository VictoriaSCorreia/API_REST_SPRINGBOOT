package com.project.MALocacao.exceptions;

public class SaidaAssociadaException extends RuntimeException {
    public SaidaAssociadaException() {
        super("Não é possível excluir esta saída pois ela está associada a um produto.");
    }
}
