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
        /*  Pega a (quantidade de unidades) que foi dada no corpo da Entrada e 
        multiplica pelo (valor unitário) do (Produto) associado para setar o (valorTotal) */
        entradaModel.setValorTotal(BigDecimal.valueOf(entradaModel.getQuantidade()).multiply(entradaModel.getProduto().getValorUnidade()));
        
        // Método já embutido no JPA
        return entradaRepository.save(entradaModel);
    }

    @Transactional
    public EntradaModel createEntrada(EntradaModel entradaModel, Long produtoId) {
        // pega o produto que veio na requisição
        Optional<ProdutoModel> produtoOptional = produtoService.findById(produtoId);
    
        // checa se o produto existe
        if (!produtoOptional.isPresent()) {
            throw new RuntimeException("Produto não encontrado com o ID: " + produtoId);
        }

         /* O valor foi retirado do Optional através do método get(), assumindo que o Optional contém um 
            valor e não deve mais ser nulo. */
        ProdutoModel produto = produtoOptional.get();
    
        // altera o (número de unidades) no produto adicionando a (quantidade) vinda na Entrada
        produto.setNumUnidades(produto.getNumUnidades() + entradaModel.getQuantidade());

        // Salva as alterações feitas no Produto
        produtoService.save(produto);

        // Finalmente associa ESSE Produto à ESSA Entrada
        entradaModel.setProduto(produto);

        // Salva a Entrada
        return save(entradaModel);
    }

    // Método já embutido no JPA
    public Page<EntradaModel> findAll(Pageable pageable) {
        return entradaRepository.findAll(pageable);
    }

    // Método já embutido no JPA
    public Optional<EntradaModel> findById(Long id) {
        return entradaRepository.findById(id);
    }
    
    // confere se a Entrada existe através do id do produto associado à ela (usado em ProdutoController)
    public boolean existsByProdutoId(Long produtoId) {
        return entradaRepository.existsByProdutoId(produtoId);
    }

    // Método já embutido no JPA
    @Transactional
    public void delete(EntradaModel entradaModel) {
        entradaRepository.delete(entradaModel);
    }
}


