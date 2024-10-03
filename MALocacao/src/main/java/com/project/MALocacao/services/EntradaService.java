package com.project.MALocacao.services;

import com.project.MALocacao.models.EntradaModel;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.EntradaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class EntradaService {

    final EntradaRepository entradaRepository;
    final ProdutoService produtoService;

    public EntradaService(EntradaRepository entradaRepository, ProdutoService produtoService) {
        this.entradaRepository = entradaRepository;
        this.produtoService = produtoService;
    }

    @Transactional
    public EntradaModel save(EntradaModel entradaModel) {
        entradaModel.setValorTotal(BigDecimal.valueOf(entradaModel.getQuantidade()).multiply(entradaModel.getProduto().getValorUnidade()));
        return entradaRepository.save(entradaModel);
    }

    @Transactional
    public EntradaModel createEntrada(EntradaModel entradaModel, Long produtoId) {
        Optional<ProdutoModel> produtoOptional = produtoService.findById(produtoId);
    
        if (!produtoOptional.isPresent()) {
            throw new RuntimeException("Produto não encontrado com o ID: " + produtoId);
        }

        ProdutoModel produto = produtoOptional.get();
        /* if (entradaModel.getQuantidade() > produto.getNumUnidades()) {
            throw new RuntimeException("Quantidade solicitada maior que unidades disponíveis no produto.");
        } */
    
        produto.setNumUnidades(produto.getNumUnidades() + entradaModel.getQuantidade());
        produtoService.save(produto);
        entradaModel.setProduto(produtoOptional.get());
        return save(entradaModel);
    }

    public Page<EntradaModel> findAll(Pageable pageable) {
        return entradaRepository.findAll(pageable);
    }

    public Optional<EntradaModel> findById(Long id) {
        return entradaRepository.findById(id);
    }
    
    public boolean existsByProdutoId(Long produtoId) {
        return entradaRepository.existsByProdutoId(produtoId);
    }

    @Transactional
    public void delete(EntradaModel entradaModel) {
        entradaRepository.delete(entradaModel);
    }
}


