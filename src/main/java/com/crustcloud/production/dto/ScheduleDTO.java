package com.crustcloud.production.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

public class ScheduleDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleResponse {
        private Long id;
        private String date;
        private List<ShiftResponse> shifts;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShiftResponse {
        private Long id;
        private String timeSlot;
        private String product;
        private Long productId;
        private String status;
        private Integer batches;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateSchedule {
        private LocalDate date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateShift {
        private Long productId;
        private String timeSlot;
        private Integer batches;
    }
}
