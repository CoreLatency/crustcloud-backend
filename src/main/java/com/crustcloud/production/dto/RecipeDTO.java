package com.crustcloud.production.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {
    private Long id;
    private String name;
    private String category;
    private String prepTime;
    private String bakingTime;
    private String yieldAmount;
    private String difficulty;
    private String image;
    private List<String> ingredients;
    private String status;
}
