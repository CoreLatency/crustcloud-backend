package com.crustcloud.production.repository;

import com.crustcloud.production.model.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, Long> {
    List<ProductionLine> findByStatus(String status);
    long countByStatus(String status);
}
