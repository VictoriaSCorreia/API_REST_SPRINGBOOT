package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public class ProductDto {

    @NotNull(message = "Necessário inserir nome corretamente")
    private String nome;

    private String setor;

    @NotNull(message = "Necessário inserir valor corretamente")
    private BigDecimal valorUnidade;

    @NotNull(message = "Necessário inserir número de unidades corretamente")
    private Long quantidadeEmEstoque; //  = 0L

    private String aplicacao;

    private String fornecedor;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSetor() {
        return setor;
    }
    public void setSetor(String setor) {
        this.setor = setor;
    }

    public BigDecimal getValorUnidade() {
        return valorUnidade;
    }
    public void setValorUnidade(BigDecimal valorUnidade) {
        this.valorUnidade = valorUnidade;
    }

    public Long getStockCount() {
        return quantidadeEmEstoque;
    }
    public void setQuantidadeEmEstoque(Long quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    public String getAplicacao() {
        return aplicacao;
    }
    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }
    
    public String getFornecedor() {
        return fornecedor;
    }
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
    
}

