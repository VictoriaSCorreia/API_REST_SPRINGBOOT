package com.project.MALocacao.exceptions;

public class SaidasAssociadasException extends RuntimeException {
    public SaidasAssociadasException() {
        super("Não é possível excluir este produto pois ele possui saidas associadas.");
    }
}
