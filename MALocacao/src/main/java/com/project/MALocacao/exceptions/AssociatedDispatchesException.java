package com.project.MALocacao.exceptions;

public class AssociatedDispatchesException extends RuntimeException {
    public AssociatedDispatchesException() {
        super("Não é possível excluir este product pois ele possui dispatches associadas.");
    }
}
