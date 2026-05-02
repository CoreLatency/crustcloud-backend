package com.crustcloud.production.repository;

import com.crustcloud.production.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findTop20ByOrderByCreatedAtDesc();
    List<Alert> findByIsReadFalseOrderByCreatedAtDesc();
    long countByIsReadFalse();
}
