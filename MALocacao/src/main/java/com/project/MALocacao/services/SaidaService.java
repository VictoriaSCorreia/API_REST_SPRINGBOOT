package com.project.MALocacao.services;

import com.project.MALocacao.models.SaidaModel;
import com.project.MALocacao.controllers.ProdutoSaidas;
import com.project.MALocacao.dtos.SaidaDto;
import com.project.MALocacao.exception.SaidaNaoEncontradaException;
import com.project.MALocacao.exception.ProdutoNaoEncontradoException;
import com.project.MALocacao.exception.QuantidadeInvalidaException;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.repositories.SaidaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
        /*  Pega a (quantidade de unidades) que foi dada no corpo da Saída e 
        multiplica pelo (valor unitário) do (Produto) associado para setar o (valorTotal) */
        saidaModel.setValorTotal(BigDecimal.valueOf(saidaModel.getQuantidade()).multiply(saidaModel.getProduto().getValorUnidade()));
        // Método já embutido no JPA
        return saidaRepository.save(saidaModel);
    }

    @Transactional
    public SaidaModel createSaida(SaidaModel saidaModel, Long produtoId) {
        // checa se o produto existe
        ProdutoModel produto = produtoService.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

        // Confere se o valor da quantidade é positivo ou menor que o estoque
        validarQuantidadeCreate(saidaModel.getQuantidade(), produto.getQuantidadeEmEstoque());

        // Altera o estoque no produto subtraindo pela quantidade vinda na Saída
        produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - saidaModel.getQuantidade());

        // Salva as alterações feitas no Produto
        produtoService.save(produto);

        // Finalmente associa ESSE Produto à ESSA Saída
        saidaModel.setProduto(produto);

        // Salva a Saida
        return save(saidaModel);
    }

    // Método já embutido no JPA
    @Transactional
    public void delete(SaidaModel saidaModel) {
        saidaRepository.delete(saidaModel);
    }

    // Método já embutido no JPA
    public Page<SaidaModel> findAll(Pageable pageable) {
        return saidaRepository.findAll(pageable);
    }

    // Método já embutido no JPA
    public Optional<SaidaModel> findById(Long id) {
        return saidaRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return saidaRepository.existsById(id);
    }

    // Método já embutido no JPA
    public void deleteById(Long id) {
        saidaRepository.deleteById(id);
    }

    public void validarQuantidadeCreate(Long quantidade, Long estoque) {
        if (quantidade > estoque || quantidade <= 0) {
            throw new QuantidadeInvalidaException();
        }
    }

    public void validarQuantidadeUpdate(Long quantidadeAnterior, Long quantidadeAtual, Long estoque) {
        if (quantidadeAtual > (estoque + quantidadeAnterior) || quantidadeAtual <= 0) {
            throw new QuantidadeInvalidaException();
        }
    }

    public void validarSaidaExiste(Long saidaId) {
        if (!existsById(saidaId)) {
            throw new SaidaNaoEncontradaException(saidaId);
        }
    }

    @Transactional
    public SaidaModel updateSaida(Long id, SaidaDto saidaDto) {
        Optional<SaidaModel> saidaModelOptional = findById(id);

        var saida = saidaModelOptional.get();

        // Pega as informações do DTO que veio no corpo da requisição e altera a SaidaModel 
        saida.setData(saidaDto.getData());
        saida.setSolicitante(saidaDto.getSolicitante());
        saida.setRequisicao(saidaDto.getRequisicao());
        saida.setLocacao(saidaDto.getLocacao());

        ProdutoModel produto = saida.getProduto();

        // Pega a quantidade anterior vinda na Saida e a nova
        Long quantidadeAnterior = saida.getQuantidade();
        Long novaQuantidade = saidaDto.getQuantidade();

        saida.setQuantidade(novaQuantidade);

        // Subtrai ou adiciona (unidades) em Produto dependendo da alteração feita em (quantidade) na Saida
        if (novaQuantidade > quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - (novaQuantidade - quantidadeAnterior));
        } else if (novaQuantidade < quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() + (quantidadeAnterior - novaQuantidade));
        }
        produtoService.save(produto);
        return save(saida);
    }

    public ProdutoSaidas getProdutoSaidas(Long produtoId) {
        var saidas = saidaRepository.getSaidasByProdutoId(produtoId);
        return new ProdutoSaidas(produtoId, List.copyOf(saidas));
    }
}



