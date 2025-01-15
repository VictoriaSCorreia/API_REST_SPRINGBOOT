package com.project.MALocacao.exceptions;

public class AssociatedInboundsException extends RuntimeException {
    public AssociatedInboundsException() {
        super("Não é possível excluir este product pois ele possui inbounds associadas.");
    }
}
