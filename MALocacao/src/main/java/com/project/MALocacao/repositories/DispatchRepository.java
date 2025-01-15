package com.project.MALocacao.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.MALocacao.models.DispatchModel;

@Repository
public interface DispatchRepository extends JpaRepository<DispatchModel, Long> {
    // confere se a Dispatch existe através do id do product associado à ela (usado em ProductController)
    boolean existsByProductId(Long productId);
    boolean existsById(Long id);
    // @Query("SELECT e FROM Dispatch e JOIN e.product p WHERE p.id = :productId")
    Set<DispatchModel> getDispatchesByProductId(@Param("productId") Long productId);
}
