package com.project.MALocacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.MALocacao.models.SaidaModel;

@Repository
public interface SaidaRepository extends JpaRepository<SaidaModel, Long> {
}
