package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.MALocacao.models.ProdutoModel;

public class EntradaDto {
    @NotNull(message = "Necessário inserir data corretamente")
    private LocalDateTime data;

    @NotNull(message = "Necessário inserir quantidade corretamente")
    private Long quantidade;
    
    @JsonIgnore
    private BigDecimal valorTotal;

    private String notaFiscal;

    @JsonIgnore
    private ProdutoModel produto;

    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getQuantidade() {
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

    public String getNotaFiscal() {
        return notaFiscal;
    }
    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public ProdutoModel getProduto() {
        return produto;
    }
    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }

}

