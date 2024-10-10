package com.project.MALocacao.services;

import com.project.MALocacao.controllers.ProdutoEntradas;
import com.project.MALocacao.dtos.EntradaDto;
import com.project.MALocacao.exception.EntradaNaoEncontradaException;
import com.project.MALocacao.exception.ProdutoNaoEncontradoException;
import com.project.MALocacao.exception.QuantidadeInvalidaException;
import com.project.MALocacao.models.EntradaModel;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.EntradaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
        /*
         * Pega a (quantidade de unidades) que foi dada no corpo da Entrada e
         * multiplica pelo (valor unitário) do (Produto) associado para setar o
         * (valorTotal)
         */
        entradaModel.setValorTotal(BigDecimal.valueOf(entradaModel.getQuantidade()).multiply(entradaModel.getProduto().getValorUnidade()));

        // Método já embutido no JPA
        return entradaRepository.save(entradaModel);
    }

    @Transactional
    public EntradaModel createEntrada(EntradaModel entradaModel, Long produtoId) {
        // checa se o produto existe
        ProdutoModel produto = produtoService.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

        /* Confere se o valor da quantidade é menor ou igual a zero (inválida) */
        validarQuantidade(entradaModel.getQuantidade());

        // altera o (número de unidades) no produto adicionando a (quantidade) vinda na Entrada
        produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() + entradaModel.getQuantidade());

        // Salva as alterações feitas no Produto
        produtoService.save(produto);

        // Finalmente associa ESSE Produto à ESSA Entrada
        entradaModel.setProduto(produto);

        // Salva a Entrada
        return save(entradaModel);
    }

    // Método já embutido no JPA
    @Transactional
    public void delete(EntradaModel entradaModel) {
        entradaRepository.delete(entradaModel);
    }

    // Método já embutido no JPA
    public Page<EntradaModel> findAll(Pageable pageable) {
        return entradaRepository.findAll(pageable);
    }

    // Método já embutido no JPA
    public Optional<EntradaModel> findById(Long id) {
        return entradaRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return entradaRepository.existsById(id);
    }

    public void deleteById(Long id) {
        entradaRepository.deleteById(id);
    }

    /* Confere se a Entrada existe através do id do produto associado à ela (usado
    em ProdutoController) */
    public boolean existsByProdutoId(Long produtoId) {
        return entradaRepository.existsByProdutoId(produtoId);
    }

    public void validarQuantidade(Long quantidade) {
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException();
        }
    }
    public void validarEntradaExiste(Long entradaId) {
        if (!existsById(entradaId)) {
            throw new EntradaNaoEncontradaException(entradaId);
        }
    }

    @Transactional
    public EntradaModel updateEntrada(Long id, EntradaDto entradaDto) {
        Optional<EntradaModel> entradaModelOptional = findById(id);

        var entrada = entradaModelOptional.get();

        // Pega as informações do DTO que veio no corpo da requisição e altera a EntradaModel 
        entrada.setData(entradaDto.getData());
        entrada.setNotaFiscal(entradaDto.getNotaFiscal());

        ProdutoModel produto = entrada.getProduto();

        // Pega a quantidade anterior vinda na Entrada e a nova
        Long quantidadeAnterior = entrada.getQuantidade();
        Long novaQuantidade = entradaDto.getQuantidade();

        entrada.setQuantidade(novaQuantidade);

        // Subtrai ou adiciona (unidades) em Produto dependendo da alteração feita em (quantidade) na Entrada
        if (novaQuantidade > quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() + (novaQuantidade - quantidadeAnterior));
        } else if (novaQuantidade < quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - (quantidadeAnterior - novaQuantidade));
        }
        produtoService.save(produto);
        return save(entrada);
    }

    // retorna o id do produto e as entradas relacionadas a ele
    public ProdutoEntradas getProdutoEntradas(Long produtoId) {
        var entradas = entradaRepository.getEntradasByProdutoId(produtoId);
        return new ProdutoEntradas(produtoId, List.copyOf(entradas));
    }
}
