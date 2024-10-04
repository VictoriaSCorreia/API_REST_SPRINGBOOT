package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.MALocacao.models.ProdutoModel;

public class SaidaDto {
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

    public ProdutoModel getProduto() {
        return produto;
    }
    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }
}

