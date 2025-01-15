package com.project.MALocacao.services;

import com.project.MALocacao.models.DispatchModel;
import com.project.MALocacao.controllers.ProductDispatches;
import com.project.MALocacao.dtos.DispatchDto;
import com.project.MALocacao.exceptions.ProductNotFoundException;
import com.project.MALocacao.exceptions.InvalidQuantityException;
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
        /*  Pega a (quantity de unidades) que foi dada no corpo da Saída e 
        multiplica pelo (valor unitário) do (Product) associado para setar o (totalValue) */
        dispatchModel.setTotalValue(BigDecimal.valueOf(dispatchModel.getQuantity()).multiply(dispatchModel.getProduct().getUnitValue()));
        // Método já embutido no JPA
        return dispatchRepository.save(dispatchModel);
    }

    @Transactional
    public DispatchModel createDispatch(DispatchModel dispatchModel, Long productId) {
        // checa se o product existe
        ProductModel product = productService.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Confere se o valor da quantity é positivo ou menor que o stock
        validateCreateQuantity(dispatchModel.getQuantity(), product.getStockCount());

        // Altera o stock no product subtraindo pela quantity vinda na Saída
        product.setStockCount(product.getStockCount() - dispatchModel.getQuantity());

        // Salva as alterações feitas no Product
        productService.save(product);

        // Finalmente associa ESSE Product à ESSA Saída
        dispatchModel.setProduct(product);

        // Salva a Saída
        return save(dispatchModel);
    }

    @Transactional
    public DispatchModel updateDispatch(Long id, DispatchDto dispatchDto) {
        Optional<DispatchModel> dispatchModelOptional = findById(id);

        var dispatch = dispatchModelOptional.get();

        // Pega as informações do DTO que veio no corpo da requisição e altera a DispatchModel 
        dispatch.setDate(dispatchDto.getDate());
        dispatch.setRequester(dispatchDto.getRequester());
        dispatch.setRequest(dispatchDto.getRequest());
        dispatch.setLocation(dispatchDto.getLocation());

        ProductModel product = dispatch.getProduct();

        // Pega a quantity anterior vinda na Dispatch e a nova
        Long previousQuantity = dispatch.getQuantity();
        Long newQuantity = dispatchDto.getQuantity();

        dispatch.setQuantity(newQuantity);

        // Subtrai ou adiciona (unidades) em Product dependendo da alteração feita em (quantity) na Dispatch
        if (newQuantity > previousQuantity) {
            product.setStockCount(product.getStockCount() - (newQuantity - previousQuantity));
        } else if (newQuantity < previousQuantity) {
            product.setStockCount(product.getStockCount() + (previousQuantity - newQuantity));
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

    public void validateCreateQuantity(Long quantity, Long stock) {
        if (quantity > stock || quantity <= 0) {
            throw new InvalidQuantityException();
        }
    }
    public void validateQuantityUpdate(Long previousQuantity, Long currentQuantity, Long stock) {
        if (currentQuantity > (stock + previousQuantity) || currentQuantity <= 0) {
            throw new InvalidQuantityException();
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



