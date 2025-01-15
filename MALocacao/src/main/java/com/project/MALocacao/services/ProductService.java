package com.project.MALocacao.services;

import com.project.MALocacao.dtos.ProductDto;
import com.project.MALocacao.exceptions.InboundsAssociadasException;
import com.project.MALocacao.exceptions.ProductJaExisteException;
import com.project.MALocacao.exceptions.ProductNotFoundException;
import com.project.MALocacao.exceptions.QuantidadeProductInvalidaException;
import com.project.MALocacao.exceptions.DispatchesAssociadasException;
import com.project.MALocacao.exceptions.ValorInvalidoException;
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
        // Confere se o product já existe pelo nome(unique)
        validarProductExisteByNome(productModel.getNome());
        // Confere se a quantidade dada de estoque é positiva
        validateQuantity(productModel.getStockCount());
        // Confere se o valor é maior que 0
        validarValor(productModel.getValorUnidade());

        // Método já embutido no JPA
        return productRepository.save(productModel);
    }

     @Transactional
    public ProductModel save(ProductModel productModel) {
        // Confere se a quantidade dada de estoque é positiva
        validateQuantity(productModel.getStockCount());
        // Método já embutido no JPA
        return productRepository.save(productModel);
    } 

    public ProductModel update(ProductModel productModel, Optional<ProductModel> productModelOptional, ProductDto productDto) {
        // valida se já há outro product com esse nome, que não seja esse mesmo
        validarAlteracaoDeNome(productDto, productModel);
        validarValor(productDto.getValorUnidade());

        /* validarAlteracaoDeEstoque(productDto, productModel); */

        Long estoque = productModel.getStockCount();
        // Pega as informações do DTO que veio no corpo da requisição e altera o ProductModel
        BeanUtils.copyProperties(productDto, productModel);

        // Mantém a quantidade em estoque anterior pois só ela pode ser alterada pela inbound e saída
        productModel.setQuantidadeEmEstoque(estoque);
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

    // Confere se o product existe através de seu nome
    public boolean existsByNome(String nome) {
        return productRepository.existsByNome(nome);
    }
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }


    public void validateProductExists(Long productId) {
        if (!existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
    }
    public void validarProductExisteByNome(String nome) {
        if (existsByNome(nome)) {
            throw new ProductJaExisteException(nome);
        }
    }
    public void validateQuantity(Long quantidade) {
        if (quantidade < 0) {
            throw new QuantidadeProductInvalidaException();
        }
    }  
    public void validarValor(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new ValorInvalidoException();
        }
    } 
    public void validateRelatedInbounds(InboundService inboundService, Long id){
        if (inboundService.existsByProductId(id)){
            throw new InboundsAssociadasException();
        }
    }
    public void validateRelatedDispatches(DispatchService dispatchService, Long id){
        if (dispatchService.existsByProductId(id)){
            throw new DispatchesAssociadasException();
        }
    }

    public void validarAlteracaoDeNome(ProductDto productDto, ProductModel productModel) {
        if (existsByNome(productDto.getNome()) && !productModel.getNome().equals(productDto.getNome())) {
            throw new ProductJaExisteException(productDto.getNome());
        }
    }
    /* public void validarAlteracaoDeEstoque(ProductDto productDto, ProductModel productModel) {
        if (productDto.getStockCount() != productModel.getStockCount()) {
            throw new AlteracaoDeEstoqueInvalidaException();
        }
    } */
}

