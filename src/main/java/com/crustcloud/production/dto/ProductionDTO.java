package com.crustcloud.production.dto;

import lombok.*;

import java.math.BigDecimal;

public class ProductionDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LineResponse {
        private Long id;
        private String name;
        private String product;
        private String status;
        private Integer progress;
        private String currentStage;
        private String batchNumber;
        private String startTime;
        private String estimatedEnd;
        private BigDecimal temperature;
        private BigDecimal humidity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusUpdate {
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProgressUpdate {
        private Integer progress;
    }
}
