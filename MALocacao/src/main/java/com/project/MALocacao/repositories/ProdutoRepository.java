package com.project.MALocacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.MALocacao.models.ProdutoModel;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
    // Confere se o produto existe através de seu nome(Usado em ProdutoController)
    boolean existsByNome(String nome);
}
