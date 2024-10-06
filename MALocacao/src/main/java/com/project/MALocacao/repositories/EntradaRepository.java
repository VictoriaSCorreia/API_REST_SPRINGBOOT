package com.project.MALocacao.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.project.MALocacao.models.EntradaModel;

@Repository
public interface EntradaRepository extends JpaRepository<EntradaModel, Long> {
    // confere se a Entrada existe através do id do produto associado à ela (usado em ProdutoController)
    boolean existsByProdutoId(Long produtoId);

    // @Query("SELECT e FROM Entrada e JOIN e.produto p WHERE p.id = :produtoId")
    Set<EntradaModel> getEntradasByProdutoId(@Param("produtoId") Long produtoId);
}
