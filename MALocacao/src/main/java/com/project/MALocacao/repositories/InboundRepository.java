package com.project.MALocacao.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.project.MALocacao.models.InboundModel;

@Repository
public interface InboundRepository extends JpaRepository<InboundModel, Long> {
    // confere se a Inbound existe através do id do product associado à ela (usado em ProductController)
    boolean existsByProductId(Long productId);
    boolean existsById(Long id);
    // @Query("SELECT e FROM Inbound e JOIN e.product p WHERE p.id = :productId")
    Set<InboundModel> getInboundsByProductId(@Param("productId") Long productId);
}
