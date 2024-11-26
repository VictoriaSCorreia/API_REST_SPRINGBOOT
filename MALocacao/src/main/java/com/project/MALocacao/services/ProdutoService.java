package com.project.MALocacao.services;

import com.project.MALocacao.exception.EntradaNaoEncontradaException;
import com.project.MALocacao.exception.ProdutoJaExisteException;
import com.project.MALocacao.exception.ProdutoNaoEncontradoException;
import com.project.MALocacao.exception.QuantidadeInvalidaException;
import com.project.MALocacao.exception.QuantidadeProdutoInvalidaException;
import com.project.MALocacao.exception.ValorInvalidoException;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        // Confere se o produto já existe pelo nome(unique)
        validarProdutoExisteByNome(produtoModel.getNome());
        // Confere se a quantidade dada de estoque é positiva
        validarQuantidade(produtoModel.getQuantidadeEmEstoque());
        // Confere se o valor é maior que 0
        validarValor(produtoModel);

        // Método já embutido no JPA
        return produtoRepository.save(produtoModel);
    }

    @Transactional
    public void delete(ProdutoModel produtoModel) {
        // Método já embutido no JPA
        produtoRepository.delete(produtoModel);
    }

    public Page<ProdutoModel> findAll(Pageable pageable) {
        // Método já embutido no JPA
        return produtoRepository.findAll(pageable);
    }
    public Optional<ProdutoModel> findById(Long id) {
        // Método já embutido no JPA
        return produtoRepository.findById(id);
    }

    // Confere se o produto existe através de seu nome(Usado em ProdutoController)
    public boolean existsByNome(String nome) {
        return produtoRepository.existsByNome(nome);
    }
    public boolean existsById(Long id) {
        return produtoRepository.existsById(id);
    }

    public void validarProdutoExiste(Long produtoId) {
        if (!existsById(produtoId)) {
            throw new ProdutoNaoEncontradoException(produtoId);
        }
    }
    public void validarProdutoExisteByNome(String nome) {
        if (existsByNome(nome)) {
            throw new ProdutoJaExisteException(nome);
        }
    }
    public void validarQuantidade(Long quantidade) {
        if (quantidade < 0) {
            throw new QuantidadeProdutoInvalidaException();
        }
    }  
    public void validarValor(ProdutoModel produto) {
        if (produto.getValorUnidade().compareTo(BigDecimal.ZERO) <= 0){
            throw new ValorInvalidoException();
        }
    } 
}

