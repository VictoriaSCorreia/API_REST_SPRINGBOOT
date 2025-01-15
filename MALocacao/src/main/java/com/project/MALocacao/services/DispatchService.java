package com.project.MALocacao.services;

import com.project.MALocacao.models.DispatchModel;
import com.project.MALocacao.controllers.ProductDispatches;
import com.project.MALocacao.dtos.DispatchDto;
import com.project.MALocacao.exceptions.ProductNotFoundException;
import com.project.MALocacao.exceptions.QuantidadeInvalidaException;
import com.project.MALocacao.exceptions.DispatchNotFoundException;
import com.project.MALocacao.models.ProductModel;
import com.project.MALocacao.repositories.DispatchRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DispatchService {
    final DispatchRepository dispatchRepository;
    final ProductService productService;

    public DispatchService(DispatchRepository dispatchRepository, ProductService productService) {
        this.dispatchRepository = dispatchRepository;
        this.productService = productService;
    }

    @Transactional
    public DispatchModel save(DispatchModel dispatchModel) {
        /*  Pega a (quantidade de unidades) que foi dada no corpo da Saída e 
        multiplica pelo (valor unitário) do (Product) associado para setar o (valorTotal) */
        dispatchModel.setValorTotal(BigDecimal.valueOf(dispatchModel.getQuantity()).multiply(dispatchModel.getProduct().getValorUnidade()));
        // Método já embutido no JPA
        return dispatchRepository.save(dispatchModel);
    }

    @Transactional
    public DispatchModel createDispatch(DispatchModel dispatchModel, Long productId) {
        // checa se o product existe
        ProductModel product = productService.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Confere se o valor da quantidade é positivo ou menor que o estoque
        validarQuantidadeCreate(dispatchModel.getQuantity(), product.getStockCount());

        // Altera o estoque no product subtraindo pela quantidade vinda na Saída
        product.setQuantidadeEmEstoque(product.getStockCount() - dispatchModel.getQuantity());

        // Salva as alterações feitas no Product
        productService.save(product);

        // Finalmente associa ESSE Product à ESSA Saída
        dispatchModel.setProduct(product);

        // Salva a Dispatch
        return save(dispatchModel);
    }

    @Transactional
    public DispatchModel updateDispatch(Long id, DispatchDto dispatchDto) {
        Optional<DispatchModel> dispatchModelOptional = findById(id);

        var dispatch = dispatchModelOptional.get();

        // Pega as informações do DTO que veio no corpo da requisição e altera a DispatchModel 
        dispatch.setData(dispatchDto.getData());
        dispatch.setSolicitante(dispatchDto.getSolicitante());
        dispatch.setRequisicao(dispatchDto.getRequisicao());
        dispatch.setLocacao(dispatchDto.getLocacao());

        ProductModel product = dispatch.getProduct();

        // Pega a quantidade anterior vinda na Dispatch e a nova
        Long quantidadeAnterior = dispatch.getQuantity();
        Long novaQuantidade = dispatchDto.getQuantity();

        dispatch.setQuantidade(novaQuantidade);

        // Subtrai ou adiciona (unidades) em Product dependendo da alteração feita em (quantidade) na Dispatch
        if (novaQuantidade > quantidadeAnterior) {
            product.setQuantidadeEmEstoque(product.getStockCount() - (novaQuantidade - quantidadeAnterior));
        } else if (novaQuantidade < quantidadeAnterior) {
            product.setQuantidadeEmEstoque(product.getStockCount() + (quantidadeAnterior - novaQuantidade));
        }
        productService.save(product);
        return save(dispatch);
    }

    // Método já embutido no JPA
    @Transactional
    public void delete(DispatchModel dispatchModel) {
        dispatchRepository.delete(dispatchModel);
    }

    public Page<DispatchModel> findAll(Pageable pageable) {
        // Método já embutido no JPA
        return dispatchRepository.findAll(pageable);
    }
    public Optional<DispatchModel> findById(Long id) {
        // Método já embutido no JPA
        return dispatchRepository.findById(id);
    }
    public void deleteById(Long id) {
        // Método já embutido no JPA
        dispatchRepository.deleteById(id);
    }
    public boolean existsById(Long id) {
        // Método já embutido no JPA
        return dispatchRepository.existsById(id);
    }

    /* Confere se a Dispatch existe através do id do product associado à ela (usado
    em ProductController) */
    public boolean existsByProductId(Long productId) {
        return dispatchRepository.existsByProductId(productId);
    }

    public void validarQuantidadeCreate(Long quantidade, Long estoque) {
        if (quantidade > estoque || quantidade <= 0) {
            throw new QuantidadeInvalidaException();
        }
    }
    public void validateQuantityUpdate(Long quantidadeAnterior, Long quantidadeAtual, Long estoque) {
        if (quantidadeAtual > (estoque + quantidadeAnterior) || quantidadeAtual <= 0) {
            throw new QuantidadeInvalidaException();
        }
    }

    public void validateDispatchExists(Long dispatchId) {
        if (!existsById(dispatchId)) {
            throw new DispatchNotFoundException(dispatchId);
        }
    }

    public ProductDispatches getProductDispatches(Long productId) {
        var dispatches = dispatchRepository.getDispatchesByProductId(productId);
        return new ProductDispatches(productId, List.copyOf(dispatches));
    }
}



