package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.MALocacao.models.ProductModel;

public class DispatchDto {
    @NotNull(message = "Necessário inserir date corretamente")
    private LocalDateTime date;

    @NotNull(message = "Necessário inserir quantity corretamente")
    private Long quantity;
    
    @JsonIgnore
    private BigDecimal totalValue;

    @NotNull(message = "Necessário inserir requester corretamente")
    private String requester;

    private String request;

    private String location;

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

    public String getRequester() {
        return requester;
    }
    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }

    public ProductModel getProduct() {
        return product;
    }
    public void setProduct(ProductModel product) {
        this.product = product;
    }
}

