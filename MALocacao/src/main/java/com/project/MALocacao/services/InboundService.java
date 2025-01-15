package com.project.MALocacao.services;

import com.project.MALocacao.controllers.ProductInbounds;
import com.project.MALocacao.dtos.InboundDto;
import com.project.MALocacao.exceptions.InboundNotFoundException;
import com.project.MALocacao.exceptions.ProductNotFoundException;
import com.project.MALocacao.exceptions.QuantidadeInvalidaException;
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
        (valorTotal) */
        inboundModel.setValorTotal(BigDecimal.valueOf(inboundModel.getQuantity()).multiply(inboundModel.getProduct().getValorUnidade()));
        // Método já embutido no JPA
        return inboundRepository.save(inboundModel);
    }

    @Transactional
    public InboundModel createInbound(InboundModel inboundModel, Long productId) {
        // checa se o product existe
        ProductModel product = productService.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        /* Confere se o valor da quantidade é positivo */
        validarQuantidade(inboundModel.getQuantity());

        // Altera o estoque no product adicionando a quantidade vinda na Saída
        product.setQuantidadeEmEstoque(product.getStockCount() + inboundModel.getQuantity());

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
        Long quantidadeAnterior = inbound.getQuantity();
        Long novaQuantidade = inboundDto.getQuantity();

        /* Valida se alteração da quantidade de uma Inbound menos(-) as quantidades retiradas nas Saídas 
        já existentes resultarão num estoque negativo */
        validateQuantityUpdate(novaQuantidade, quantidadeAnterior, product.getStockCount());

        // Subtrai ou adiciona (estoque) em Product dependendo da alteração feita em (quantidade) na Inbound
        if (novaQuantidade > quantidadeAnterior) {
            product.setQuantidadeEmEstoque(product.getStockCount() + (novaQuantidade - quantidadeAnterior));
        } else if (novaQuantidade < quantidadeAnterior) {
            product.setQuantidadeEmEstoque(product.getStockCount() - (quantidadeAnterior - novaQuantidade));
        }

        // Pega as informações do DTO que veio no corpo da requisição e altera a InboundModel 
        inbound.setQuantidade(novaQuantidade);
        inbound.setData(inboundDto.getData());
        inbound.setNotaFiscal(inboundDto.getNotaFiscal());

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

    public void validarInboundExiste(Long inboundId) {
        if (!existsById(inboundId)) {
            throw new InboundNotFoundException(inboundId);
        }
    }

    public void validarQuantidade(Long quantidade) {
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException();
        }
    }
    public void validateQuantityUpdate(Long novaQuantidade, Long quantidadeAnterior, Long estoque){
        if (estoque + (novaQuantidade - quantidadeAnterior) < 0){
            throw new QuantidadeInvalidaException();
        }
    }
    // retorna o id do product e as inbounds relacionadas a ele
    public ProductInbounds getProductInbounds(Long productId) {
        var inbounds = inboundRepository.getInboundsByProductId(productId);
        return new ProductInbounds(productId, List.copyOf(inbounds));
    }
}
