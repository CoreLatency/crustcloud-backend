package com.crustcloud.production.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDTO {
    private Long id;
    private String type;
    private String message;
    private String time;
    private Boolean isRead;
}
