package com.project.MALocacao.exception;

public class EntradasAssociadasException extends RuntimeException {
    public EntradasAssociadasException() {
        super("Não é possível excluir este produto pois ele possui entradas associadas.");
    }
}
