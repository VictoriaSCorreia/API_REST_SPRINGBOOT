package com.project.MALocacao.exceptions;

public class InvalidStockModificationException extends RuntimeException {
    public InvalidStockModificationException() {
        super("O número de unidades em estoque só pode ser alterado através da Inbound e Saída.");
    }
}

