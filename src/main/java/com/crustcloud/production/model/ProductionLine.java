package com.crustcloud.production.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_product_id")
    private Product currentProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_batch_id")
    private ProductionBatch currentBatch;

    @Column(nullable = false)
    private String status;

    private Integer progress;

    @Column(name = "current_stage")
    private String currentStage;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "estimated_end")
    private String estimatedEnd;

    private BigDecimal temperature;

    private BigDecimal humidity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "stopped";
        if (progress == null) progress = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
