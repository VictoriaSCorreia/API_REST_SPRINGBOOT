package com.project.MALocacao.services;

import com.project.MALocacao.controllers.ProductInbounds;
import com.project.MALocacao.dtos.InboundDto;
import com.project.MALocacao.exceptions.InboundNotFoundException;
import com.project.MALocacao.exceptions.ProductNotFoundException;
import com.project.MALocacao.exceptions.InvalidQuantityException;
import com.project.MALocacao.models.InboundModel;
import com.project.MALocacao.models.ProductModel;
import com.project.MALocacao.repositories.InboundRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InboundService {

    final InboundRepository inboundRepository;
    final ProductService productService;

    public InboundService(InboundRepository inboundRepository, ProductService productService) {
        this.inboundRepository = inboundRepository;
        this.productService = productService;
    }

    @Transactional
    public InboundModel save(InboundModel inboundModel) {
        /* Pega a (quantidade de unidades) que foi dada no corpo da Inbound e
        multiplica pelo (valor unitário) do (Product) associado para setar o
        (totalValue) */
        inboundModel.setTotalValue(BigDecimal.valueOf(inboundModel.getQuantity()).multiply(inboundModel.getProduct().getUnitValue()));
        // Método já embutido no JPA
        return inboundRepository.save(inboundModel);
    }

    @Transactional
    public InboundModel createInbound(InboundModel inboundModel, Long productId) {
        // checa se o product existe
        ProductModel product = productService.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        /* Confere se o valor da quantidade é positivo */
        validateQuantity(inboundModel.getQuantity());

        // Altera o stock no product adicionando a quantidade vinda na Saída
        product.setStockCount(product.getStockCount() + inboundModel.getQuantity());

        // Salva as alterações feitas no Product
        productService.save(product);

        // Finalmente associa ESSE Product à ESSA Inbound
        inboundModel.setProduct(product);

        // Salva a Inbound
        return save(inboundModel);
    }

    @Transactional
    public InboundModel updateInbound(Long id, InboundDto inboundDto) {
        Optional<InboundModel> inboundModelOptional = findById(id);
        var inbound = inboundModelOptional.get();
        ProductModel product = inbound.getProduct();

        // Pega a quantidade anterior vinda na Inbound e a nova
        Long previousQuantity = inbound.getQuantity();
        Long newQuantity = inboundDto.getQuantity();

        /* Valida se alteração da quantidade de uma Inbound menos(-) as quantidades retiradas nas Saídas 
        já existentes resultarão num stock negativo */
        validateQuantityUpdate(newQuantity, previousQuantity, product.getStockCount());

        // Subtrai ou adiciona (stock) em Product dependendo da alteração feita em (quantidade) na Inbound
        if (newQuantity > previousQuantity) {
            product.setStockCount(product.getStockCount() + (newQuantity - previousQuantity));
        } else if (newQuantity < previousQuantity) {
            product.setStockCount(product.getStockCount() - (previousQuantity - newQuantity));
        }

        // Pega as informações do DTO que veio no corpo da requisição e altera a InboundModel 
        inbound.setQuantity(newQuantity);
        inbound.setDate(inboundDto.getDate());
        inbound.setInvoice(inboundDto.getInvoice());

        productService.save(product);
        return save(inbound);
    }
    
    // Método já embutido no JPA
    @Transactional
    public void delete(InboundModel inboundModel) {
        inboundRepository.delete(inboundModel);
    }

    public Page<InboundModel> findAll(Pageable pageable) {
        // Método já embutido no JPA
        return inboundRepository.findAll(pageable);
    }
    public Optional<InboundModel> findById(Long id) {
        // Método já embutido no JPA
        return inboundRepository.findById(id);
    }
    public void deleteById(Long id) {
        // Método já embutido no JPA
        inboundRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return inboundRepository.existsById(id);
    }
    /* Confere se a Inbound existe através do id do product associado à ela (usado
    em ProductController) */
    public boolean existsByProductId(Long productId) {
        return inboundRepository.existsByProductId(productId);
    }

    public void validateInboundExists(Long inboundId) {
        if (!existsById(inboundId)) {
            throw new InboundNotFoundException(inboundId);
        }
    }

    public void validateQuantity(Long quantidade) {
        if (quantidade <= 0) {
            throw new InvalidQuantityException();
        }
    }
    public void validateQuantityUpdate(Long newQuantity, Long previousQuantity, Long stock){
        if (stock + (newQuantity - previousQuantity) < 0){
            throw new InvalidQuantityException();
        }
    }
    // retorna o id do product e as inbounds relacionadas a ele
    public ProductInbounds getProductInbounds(Long productId) {
        var inbounds = inboundRepository.getInboundsByProductId(productId);
        return new ProductInbounds(productId, List.copyOf(inbounds));
    }
}
