package com.project.MALocacao.services;

import com.project.MALocacao.dtos.ProdutoDto;
import com.project.MALocacao.exceptions.AlteracaoDeEstoqueInvalidaException;
import com.project.MALocacao.exceptions.EntradasAssociadasException;
import com.project.MALocacao.exceptions.ProdutoJaExisteException;
import com.project.MALocacao.exceptions.ProdutoNaoEncontradoException;
import com.project.MALocacao.exceptions.QuantidadeProdutoInvalidaException;
import com.project.MALocacao.exceptions.SaidasAssociadasException;
import com.project.MALocacao.exceptions.ValorInvalidoException;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.ProdutoRepository;

import org.springframework.beans.BeanUtils;
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
    public ProdutoModel create(ProdutoModel produtoModel) {
        // Confere se o produto já existe pelo nome(unique)
        validarProdutoExisteByNome(produtoModel.getNome());
        // Confere se a quantidade dada de estoque é positiva
        validarQuantidade(produtoModel.getQuantidadeEmEstoque());
        // Confere se o valor é maior que 0
        validarValor(produtoModel.getValorUnidade());

        // Método já embutido no JPA
        return produtoRepository.save(produtoModel);
    }

     @Transactional
    public ProdutoModel save(ProdutoModel produtoModel) {
        // Confere se a quantidade dada de estoque é positiva
        validarQuantidade(produtoModel.getQuantidadeEmEstoque());
        // Método já embutido no JPA
        return produtoRepository.save(produtoModel);
    } 

    public ProdutoModel update(ProdutoModel produtoModel, Optional<ProdutoModel> produtoModelOptional, ProdutoDto produtoDto) {
        // valida se já há outro produto com esse nome, que não seja esse mesmo
        validarAlteracaoDeNome(produtoDto, produtoModel);
        validarValor(produtoDto.getValorUnidade());

        /* validarAlteracaoDeEstoque(produtoDto, produtoModel); */

        Long estoque = produtoModel.getQuantidadeEmEstoque();
        // Pega as informações do DTO que veio no corpo da requisição e altera o ProdutoModel
        BeanUtils.copyProperties(produtoDto, produtoModel);

        // Mantém a quantidade em estoque anterior pois só ela pode ser alterada pela entrada e saída
        produtoModel.setQuantidadeEmEstoque(estoque);
        // Precisa setar o ID manualmente pois o DTO não possui esse campo(ele é gerado automaticamente no Model)
        produtoModel.setId(produtoModelOptional.get().getId());

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
    public void deleteById(Long id) {
        // Método já embutido no JPA
        produtoRepository.deleteById(id);
    }

    // Confere se o produto existe através de seu nome
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
    public void validarValor(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new ValorInvalidoException();
        }
    } 
    public void validarEntradasAssociadas(EntradaService entradaService, Long id){
        if (entradaService.existsByProdutoId(id)){
            throw new EntradasAssociadasException();
        }
    }
    public void validarSaidasAssociadas(SaidaService saidaService, Long id){
        if (saidaService.existsByProdutoId(id)){
            throw new SaidasAssociadasException();
        }
    }

    public void validarAlteracaoDeNome(ProdutoDto produtoDto, ProdutoModel produtoModel) {
        if (existsByNome(produtoDto.getNome()) && !produtoModel.getNome().equals(produtoDto.getNome())) {
            throw new ProdutoJaExisteException(produtoDto.getNome());
        }
    }
    /* public void validarAlteracaoDeEstoque(ProdutoDto produtoDto, ProdutoModel produtoModel) {
        if (produtoDto.getQuantidadeEmEstoque() != produtoModel.getQuantidadeEmEstoque()) {
            throw new AlteracaoDeEstoqueInvalidaException();
        }
    } */
}

