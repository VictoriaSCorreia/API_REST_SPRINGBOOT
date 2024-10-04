package com.project.MALocacao.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUTO")
public class ProdutoModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, length = 100)
    private String nome;

    @Column(length = 50)
    private String setor;

    @Column(scale = 2)
    private BigDecimal valorUnidade;

    @Column()
    private Long numUnidades;

    @Column(length = 50)
    private String aplicacao;

    @Column(length = 30)
    private String fornecedor;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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

