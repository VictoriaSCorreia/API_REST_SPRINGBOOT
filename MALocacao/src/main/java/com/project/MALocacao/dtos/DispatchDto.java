package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.MALocacao.models.ProductModel;

public class DispatchDto {
    @NotNull(message = "Necessário inserir data corretamente")
    private LocalDateTime data;

    @NotNull(message = "Necessário inserir quantidade corretamente")
    private Long quantidade;
    
    @JsonIgnore
    private BigDecimal valorTotal;

    @NotNull(message = "Necessário inserir solicitante corretamente")
    private String solicitante;

    private String requisicao;

    private String locacao;

    @JsonIgnore
    private ProductModel product;

    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getQuantity() {
        return quantidade;
    }
    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getSolicitante() {
        return solicitante;
    }
    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getLocacao() {
        return locacao;
    }
    public void setLocacao(String locacao) {
        this.locacao = locacao;
    }

    public String getRequisicao() {
        return requisicao;
    }
    public void setRequisicao(String requisicao) {
        this.requisicao = requisicao;
    }

    public ProductModel getProduct() {
        return product;
    }
    public void setProduct(ProductModel product) {
        this.product = product;
    }
}

