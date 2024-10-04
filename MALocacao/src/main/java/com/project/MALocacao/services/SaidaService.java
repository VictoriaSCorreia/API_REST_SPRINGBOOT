package com.project.MALocacao.services;

import com.project.MALocacao.models.SaidaModel;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.SaidaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SaidaService {
    final SaidaRepository saidaRepository;
    final ProdutoService produtoService;

    public SaidaService(SaidaRepository saidaRepository, ProdutoService produtoService) {
        this.saidaRepository = saidaRepository;
        this.produtoService = produtoService;
    }

    @Transactional
    public SaidaModel save(SaidaModel saidaModel) {
        saidaModel.setValorTotal(BigDecimal.valueOf(saidaModel.getQuantidade()).multiply(saidaModel.getProduto().getValorUnidade()));
        return saidaRepository.save(saidaModel);
    }

    @Transactional
    public SaidaModel createSaida(SaidaModel saidaModel, Long produtoId) {
        Optional<ProdutoModel> produtoOptional = produtoService.findById(produtoId);
    
        if (!produtoOptional.isPresent()) {
            throw new RuntimeException("Produto não encontrado com o ID: " + produtoId);
        }

        ProdutoModel produto = produtoOptional.get();
        if (saidaModel.getQuantidade() > produto.getNumUnidades() || saidaModel.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade solicitada inválida ou maior que unidades disponíveis no produto.");
        }
        produto.setNumUnidades(produto.getNumUnidades() - saidaModel.getQuantidade());
        produtoService.save(produto);
        saidaModel.setProduto(produtoOptional.get());
        return save(saidaModel);
    }

    public Page<SaidaModel> findAll(Pageable pageable) {
        return saidaRepository.findAll(pageable);
    }

    public Optional<SaidaModel> findById(Long id) {
        return saidaRepository.findById(id);
    }
    
    public boolean existsByProdutoId(Long produtoId) {
        return saidaRepository.existsByProdutoId(produtoId);
    }

    @Transactional
    public void delete(SaidaModel saidaModel) {
        saidaRepository.delete(saidaModel);
    }
}



