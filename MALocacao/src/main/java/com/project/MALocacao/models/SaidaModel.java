package com.project.MALocacao.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "SAIDA")
public class SaidaModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private LocalDateTime data;

    @Column()
    private Long quantidade;

    @Column(scale = 2)
    private BigDecimal valorTotal;

    @OneToOne(cascade = CascadeType.ALL)
    private ProdutoModel produto;

    @Column(length = 50)
    private String solicitante;

    @Column(length = 50)
    private String locacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
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

}

