package com.project.MALocacao.exceptions;

public class InboundsAssociadasException extends RuntimeException {
    public InboundsAssociadasException() {
        super("Não é possível excluir este product pois ele possui inbounds associadas.");
    }
}
