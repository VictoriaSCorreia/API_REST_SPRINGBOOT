package com.project.MALocacao.dtos;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public class ProductDto {

    @NotNull(message = "Necessário inserir name corretamente")
    private String name;

    private String sector;

    @NotNull(message = "Necessário inserir valor corretamente")
    private BigDecimal unitValue;

    @NotNull(message = "Necessário inserir número de unidades corretamente")
    private Long stockCount; //  = 0L

    private String aplication;

    private String supplier;

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

