package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.MALocacao.models.ProductModel;

public class InboundDto {
    @NotNull(message = "Necessário inserir date corretamente")
    private LocalDateTime date = LocalDateTime.now();

    @NotNull(message = "Necessário inserir quantity corretamente")
    private Long quantity;
    
    @JsonIgnore
    private BigDecimal totalValue;

    private String invoice;

    @JsonIgnore
    private ProductModel product;

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getQuantity() {
        return quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public String getInvoice() {
        return invoice;
    }
    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public ProductModel getProduct() {
        return product;
    }
    public void setProduct(ProductModel product) {
        this.product = product;
    }

}

