package com.project.MALocacao.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product não encontrado com o ID: " + productId);
    }
}
