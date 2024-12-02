package com.project.MALocacao.exceptions;

public class SaidaNaoEncontradaException extends RuntimeException {
    public SaidaNaoEncontradaException(Long saidaId) {
        super("Saida não encontrada com o ID: " + saidaId);
    }
}
