package com.project.MALocacao.services;

import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProdutoService {

    final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoModel save(ProdutoModel produtoModel) {
        if (produtoModel.getValorUnidade().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException();
        }
        // Método já embutido no JPA
        return produtoRepository.save(produtoModel);
    }

    // Confere se o produto existe através de seu nome(Usado em ProdutoController)
    public boolean existsByNome(String nome) {
        return produtoRepository.existsByNome(nome);
    }

    public Page<ProdutoModel> findAll(Pageable pageable) {
        // Método já embutido no JPA
        return produtoRepository.findAll(pageable);
    }

    public Optional<ProdutoModel> findById(Long id) {
        // Método já embutido no JPA
        return produtoRepository.findById(id);
    }
    @Transactional
    public void delete(ProdutoModel produtoModel) {
        // Método já embutido no JPA
        produtoRepository.delete(produtoModel);
    }
}

