package com.project.MALocacao.exceptions;

public class EntradaNaoEncontradaException extends RuntimeException {
    public EntradaNaoEncontradaException(Long entradaId) {
        super("Entrada não encontrada com o ID: " + entradaId);
    }
}
