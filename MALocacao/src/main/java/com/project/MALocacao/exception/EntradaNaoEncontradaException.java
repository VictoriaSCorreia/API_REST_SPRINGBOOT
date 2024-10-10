package com.project.MALocacao.exception;

public class EntradaNaoEncontradaException extends RuntimeException {
    public EntradaNaoEncontradaException(Long entradaId) {
        super("Entrada não encontrada com o ID: " + entradaId);
    }
}
