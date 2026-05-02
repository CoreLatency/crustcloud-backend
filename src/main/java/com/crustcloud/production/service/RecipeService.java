package com.crustcloud.production.service;

import com.crustcloud.production.dto.RecipeDTO;
import com.crustcloud.production.model.Product;
import com.crustcloud.production.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ProductRepository productRepository;

    public List<RecipeDTO> getAllRecipes() {
        return productRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<RecipeDTO> getRecipesByCategory(String category) {
        return productRepository.findByCategory(category).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<RecipeDTO> searchRecipes(String query) {
        return productRepository.findByNameContainingIgnoreCase(query).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public RecipeDTO getRecipeById(Long id) {
        return productRepository.findById(id)
            .map(this::mapToDTO)
            .orElse(null);
    }

    @Transactional
    public RecipeDTO createRecipe(RecipeDTO dto) {
        Product product = Product.builder()
            .name(dto.getName())
            .category(dto.getCategory())
            .prepTime(dto.getPrepTime())
            .bakingTime(dto.getBakingTime())
            .yieldAmount(dto.getYieldAmount())
            .difficulty(dto.getDifficulty())
            .image(dto.getImage())
            .ingredients(String.join(",", dto.getIngredients()))
            .status(dto.getStatus() != null ? dto.getStatus() : "active")
            .build();

        return mapToDTO(productRepository.save(product));
    }

    @Transactional
    public RecipeDTO updateRecipe(Long id, RecipeDTO dto) {
        return productRepository.findById(id)
            .map(product -> {
                product.setName(dto.getName());
                product.setCategory(dto.getCategory());
                product.setPrepTime(dto.getPrepTime());
                product.setBakingTime(dto.getBakingTime());
                product.setYieldAmount(dto.getYieldAmount());
                product.setDifficulty(dto.getDifficulty());
                product.setImage(dto.getImage());
                product.setIngredients(String.join(",", dto.getIngredients()));
                product.setStatus(dto.getStatus());
                return mapToDTO(productRepository.save(product));
            })
            .orElse(null);
    }

    @Transactional
    public boolean deleteRecipe(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private RecipeDTO mapToDTO(Product product) {
        List<String> ingredients = product.getIngredients() != null
            ? Arrays.asList(product.getIngredients().split(","))
            : new ArrayList<>();

        return RecipeDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .category(product.getCategory())
            .prepTime(product.getPrepTime())
            .bakingTime(product.getBakingTime())
            .yieldAmount(product.getYieldAmount())
            .difficulty(product.getDifficulty() != null ? product.getDifficulty() : "Easy")
            .image(product.getImage())
            .ingredients(ingredients)
            .status(product.getStatus() != null ? product.getStatus() : "active")
            .build();
    }
}
