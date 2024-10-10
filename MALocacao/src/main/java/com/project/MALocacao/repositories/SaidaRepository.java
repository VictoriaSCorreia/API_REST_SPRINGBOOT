package com.project.MALocacao.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.MALocacao.models.SaidaModel;
import com.project.MALocacao.models.SaidaModel;

@Repository
public interface SaidaRepository extends JpaRepository<SaidaModel, Long> {
    // @Query("SELECT e FROM Saida e JOIN e.produto p WHERE p.id = :produtoId")
    Set<SaidaModel> getSaidasByProdutoId(@Param("produtoId") Long produtoId);
}
