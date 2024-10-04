package com.project.MALocacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.MALocacao.models.EntradaModel;

@Repository
public interface EntradaRepository extends JpaRepository<EntradaModel, Long> {
    // confere se a Entrada existe através do id do produto associado à ela (usado em ProdutoController)
    boolean existsByProdutoId(Long produtoId);
}
