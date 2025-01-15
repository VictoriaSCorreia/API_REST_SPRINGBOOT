package com.project.MALocacao.exceptions;

public class InboundNotFoundException extends RuntimeException {
    public InboundNotFoundException(Long inboundId) {
        super("Inbound não encontrada com o ID: " + inboundId);
    }
}
