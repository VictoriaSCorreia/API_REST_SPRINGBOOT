package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public class ProdutoDto {

    @NotNull(message = "Necessário inserir nome corretamente")
    private String nome;

    private String setor;

    @NotNull(message = "Necessário inserir valor corretamente")
    private BigDecimal valorUnidade;

    @NotNull(message = "Necessário inserir número de unidades corretamente")
    private Long numUnidades;

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
    public Long getNumUnidades() {
        return numUnidades;
    }
    public void setNumUnidades(Long numUnidades) {
        this.numUnidades = numUnidades;
    }
    
}

