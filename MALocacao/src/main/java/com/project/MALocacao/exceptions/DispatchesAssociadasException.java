package com.project.MALocacao.exceptions;

public class DispatchesAssociadasException extends RuntimeException {
    public DispatchesAssociadasException() {
        super("Não é possível excluir este product pois ele possui dispatches associadas.");
    }
}
