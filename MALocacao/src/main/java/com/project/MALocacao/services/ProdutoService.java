package com.project.MALocacao.services;

import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class ProdutoService {

    final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoModel save(ProdutoModel produtoModel) {
        return produtoRepository.save(produtoModel);
    }

    public boolean existsByNome(String nome) {
        return produtoRepository.existsByNome(nome);
    }

    public Page<ProdutoModel> findAll(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Optional<ProdutoModel> findById(Long id) {
        return produtoRepository.findById(id);
    }

    @Transactional
    public void delete(ProdutoModel produtoModel) {
        produtoRepository.delete(produtoModel);
    }
}

