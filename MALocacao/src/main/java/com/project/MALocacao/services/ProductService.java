package com.project.MALocacao.services;

import com.project.MALocacao.dtos.ProductDto;
import com.project.MALocacao.exceptions.AssociatedInboundsException;
import com.project.MALocacao.exceptions.ProductAlreadyExistsException;
import com.project.MALocacao.exceptions.ProductNotFoundException;
import com.project.MALocacao.exceptions.InvalidProductQuantityException;
import com.project.MALocacao.exceptions.AssociatedDispatchesException;
import com.project.MALocacao.exceptions.InvalidValueException;
import com.project.MALocacao.models.ProductModel;
import com.project.MALocacao.repositories.ProductRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductModel create(ProductModel productModel) {
        // Confere se o product já existe pelo name(unique)
        validateProductExistsByName(productModel.getName());
        // Confere se a quantidade dada de stock é positiva
        validateQuantity(productModel.getStockCount());
        // Confere se o valor é maior que 0
        validateValue(productModel.getUnitValue());

        // Método já embutido no JPA
        return productRepository.save(productModel);
    }

     @Transactional
    public ProductModel save(ProductModel productModel) {
        // Confere se a quantidade dada de stock é positiva
        validateQuantity(productModel.getStockCount());
        // Método já embutido no JPA
        return productRepository.save(productModel);
    } 

    public ProductModel update(ProductModel productModel, Optional<ProductModel> productModelOptional, ProductDto productDto) {
        // valida se já há outro product com esse name, que não seja esse mesmo
        validateNameModification(productDto, productModel);
        validateValue(productDto.getUnitValue());

        /* validarAlteracaoDeEstoque(productDto, productModel); */

        Long stock = productModel.getStockCount();
        // Pega as informações do DTO que veio no corpo da requisição e altera o ProductModel
        BeanUtils.copyProperties(productDto, productModel);

        // Mantém a quantidade em stock anterior pois só ela pode ser alterada pela inbound e saída
        productModel.setStockCount(stock);
        // Precisa setar o ID manualmente pois o DTO não possui esse campo(ele é gerado automaticamente no Model)
        productModel.setId(productModelOptional.get().getId());

        // Método já embutido no JPA
        return productRepository.save(productModel);
    }

    @Transactional
    public void delete(ProductModel productModel) {
        // Método já embutido no JPA
        productRepository.delete(productModel);
    }

    public Page<ProductModel> findAll(Pageable pageable) {
        // Método já embutido no JPA
        return productRepository.findAll(pageable);
    }
    public Optional<ProductModel> findById(Long id) {
        // Método já embutido no JPA
        return productRepository.findById(id);
    }
    public void deleteById(Long id) {
        // Método já embutido no JPA
        productRepository.deleteById(id);
    }

    // Confere se o product existe através de seu name
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }


    public void validateProductExists(Long productId) {
        if (!existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
    }
    public void validateProductExistsByName(String name) {
        if (existsByName(name)) {
            throw new ProductAlreadyExistsException(name);
        }
    }
    public void validateQuantity(Long quantidade) {
        if (quantidade < 0) {
            throw new InvalidProductQuantityException();
        }
    }  
    public void validateValue(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidValueException();
        }
    } 
    public void validateRelatedInbounds(InboundService inboundService, Long id){
        if (inboundService.existsByProductId(id)){
            throw new AssociatedInboundsException();
        }
    }
    public void validateRelatedDispatches(DispatchService dispatchService, Long id){
        if (dispatchService.existsByProductId(id)){
            throw new AssociatedDispatchesException();
        }
    }

    public void validateNameModification(ProductDto productDto, ProductModel productModel) {
        if (existsByName(productDto.getName()) && !productModel.getName().equals(productDto.getName())) {
            throw new ProductAlreadyExistsException(productDto.getName());
        }
    }
    /* public void validarAlteracaoDeEstoque(ProductDto productDto, ProductModel productModel) {
        if (productDto.getStockCount() != productModel.getStockCount()) {
            throw new InvalidStockModificationException();
        }
    } */
}

