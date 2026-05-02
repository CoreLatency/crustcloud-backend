package com.crustcloud.production.repository;

import com.crustcloud.production.model.ProductionBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, Long> {
    Optional<ProductionBatch> findByBatchNumber(String batchNumber);
    List<ProductionBatch> findByStatus(String status);

    @Query("SELECT pb FROM ProductionBatch pb WHERE DATE(pb.planDate) = DATE(:date)")
    List<ProductionBatch> findByPlanDate(@Param("date") LocalDateTime date);

    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.planDate >= :startDate AND pb.planDate <= :endDate ORDER BY pb.planDate ASC")
    List<ProductionBatch> findByPlanDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(pb) FROM ProductionBatch pb WHERE DATE(pb.planDate) = DATE(:date)")
    Long countByPlanDate(@Param("date") LocalDateTime date);

    @Query("SELECT COALESCE(SUM(pb.totalQty), 0) FROM ProductionBatch pb WHERE DATE(pb.planDate) = DATE(:date)")
    Integer sumTotalQtyByPlanDate(@Param("date") LocalDateTime date);

    @Query("SELECT MIN(pb.startSponge) FROM ProductionBatch pb WHERE DATE(pb.planDate) = DATE(:date)")
    String findFirstStartByPlanDate(@Param("date") LocalDateTime date);

    @Query("SELECT MAX(pb.endBatch) FROM ProductionBatch pb WHERE DATE(pb.planDate) = DATE(:date)")
    String findLastEndByPlanDate(@Param("date") LocalDateTime date);
}
