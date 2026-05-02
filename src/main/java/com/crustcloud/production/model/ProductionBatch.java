package com.crustcloud.production.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_number", nullable = false, unique = true)
    private String batchNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String status;

    @Column(name = "sales_order")
    private String salesOrder;

    @Column(name = "total_qty")
    private Integer totalQty;

    @Column(name = "so_co_excess")
    private Integer soCoExcess;

    @Column(name = "exch_loss")
    private Integer exchLoss;

    private Integer excess;

    private Integer samples;

    @Column(name = "carry_over")
    private Integer carryOver;

    @Column(name = "theor_excess")
    private Integer theorExcess;

    @Column(name = "batch_qty")
    private Integer batchQty;

    private Integer capacity;

    private String dough;

    @Column(name = "proc_time")
    private Integer procTime;

    @Column(name = "start_sponge")
    private String startSponge;

    @Column(name = "end_dough")
    private String endDough;

    @Column(name = "end_batch")
    private String endBatch;

    @Column(name = "order_batch")
    private String orderBatch;

    @Column(name = "line_batch")
    private String lineBatch;

    @Column(name = "plan_date")
    private LocalDateTime planDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "Waiting";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getDisplayStatus() {
        if ("InProgress".equals(status)) return "In Progress";
        return status;
    }
}
