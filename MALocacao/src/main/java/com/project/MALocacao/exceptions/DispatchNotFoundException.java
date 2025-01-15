package com.project.MALocacao.exceptions;

public class DispatchNotFoundException extends RuntimeException {
    public DispatchNotFoundException(Long dispatchId) {
        super("Dispatch não encontrada com o ID: " + dispatchId);
    }
}
