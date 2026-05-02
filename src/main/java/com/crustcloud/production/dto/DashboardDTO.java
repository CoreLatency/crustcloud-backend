package com.crustcloud.production.dto;

import lombok.*;
import java.util.List;

public class DashboardDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Stats {
        private Integer totalBatches;
        private String totalOutput;
        private String planDate;
        private String firstStart;
        private String lastEnd;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GanttProduct {
        private String id;
        private String name;
        private List<TimelineSegment> timeline;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimelineSegment {
        private String stage;
        private Integer start;
        private Integer width;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OutputProduct {
        private String id;
        private String name;
        private Integer output;
        private Integer maxOutput;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TableRow {
        private String id;
        private String status;
        private String product;
        private String salesOrder;
        private Integer totalQty;
        private Integer soCoExcess;
        private Integer exchLoss;
        private Integer excess;
        private Integer samples;
        private Integer carryOver;
        private Integer theorExcess;
        private Integer batchQty;
        private Integer capacity;
        private String dough;
        private Integer procTime;
        private String startSponge;
        private String endDough;
        private String endBatch;
        private String orderBatch;
        private String lineBatch;
    }
}
