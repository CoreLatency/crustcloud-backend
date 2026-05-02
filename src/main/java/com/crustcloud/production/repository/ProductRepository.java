package com.crustcloud.production.repository;

import com.crustcloud.production.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByStatus(String status);
    List<Product> findByNameContainingIgnoreCase(String name);
}
