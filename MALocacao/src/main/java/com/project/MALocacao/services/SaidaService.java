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
        /*  Pega a (quantidade de unidades) que foi dada no corpo da Saída e 
        multiplica pelo (valor unitário) do (Produto) associado para setar o (valorTotal) */
        saidaModel.setValorTotal(BigDecimal.valueOf(saidaModel.getQuantidade()).multiply(saidaModel.getProduto().getValorUnidade()));
        return saidaRepository.save(saidaModel);
    }

    @Transactional
    public SaidaModel createSaida(SaidaModel saidaModel, Long produtoId) {
         // pega o produto que veio na requisição
        Optional<ProdutoModel> produtoOptional = produtoService.findById(produtoId);
    
        // checa se o produto existe
        if (!produtoOptional.isPresent()) {
            throw new RuntimeException("Produto não encontrado com o ID: " + produtoId);
        }

        /* O valor foi retirado do Optional através do método get(), assumindo que o Optional contém um 
            valor e não deve mais ser nulo. */
        ProdutoModel produto = produtoOptional.get();

        /* Confere se a (quantidade) a ser retirada é maior que o estoque (NumUnidades) ou se o 
        valor dela é menor ou igual a zero (inválida)*/
        if (saidaModel.getQuantidade() > produto.getNumUnidades() || saidaModel.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade solicitada inválida ou maior que o estoque disponível do produto.");
        }

        // altera o (número de unidades) no produto removendo pela (quantidade) vinda na Saída
        produto.setNumUnidades(produto.getNumUnidades() - saidaModel.getQuantidade());

        // Salva as alterações feitas no Produto
        produtoService.save(produto);

        // Finalmente associa ESSE Produto à ESSA Saída
        saidaModel.setProduto(produto);

        // Salva a Entrada
        return save(saidaModel);
    }

    // Método já embutido no JPA
    public Page<SaidaModel> findAll(Pageable pageable) {
        return saidaRepository.findAll(pageable);
    }

    // Método já embutido no JPA
    public Optional<SaidaModel> findById(Long id) {
        return saidaRepository.findById(id);
    }

    // Método já embutido no JPA
    @Transactional
    public void delete(SaidaModel saidaModel) {
        saidaRepository.delete(saidaModel);
    }
}



