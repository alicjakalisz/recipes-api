package com.alicja.recipes.configuration;


import com.alicja.recipes.converter.RecipeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public RecipeMapper recipeMapper() {
        return new RecipeMapper();
    }
}