package com.project.MALocacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.MALocacao.models.EntradaModel;

@Repository
public interface EntradaRepository extends JpaRepository<EntradaModel, Long> {
    boolean existsByProdutoId(Long produtoId);
}
