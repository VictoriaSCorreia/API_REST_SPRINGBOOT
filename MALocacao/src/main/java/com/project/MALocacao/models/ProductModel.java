package com.project.MALocacao.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT")
public class ProductModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String sector;

    @Column(scale = 2)
    private BigDecimal unitValue;

    @Column()
    private Long stockCount;

    @Column(length = 50)
    private String aplication;

    @Column(length = 30)
    private String supplier;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }
    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public Long getStockCount() {
        return stockCount;
    }
    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public String getAplication() {
        return aplication;
    }
    public void setAplication(String aplication) {
        this.aplication = aplication;
    }

    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}


