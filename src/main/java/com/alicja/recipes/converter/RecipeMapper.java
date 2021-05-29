package com.alicja.recipes.converter;

import com.alicja.recipes.dto.RecipeDto;
import com.alicja.recipes.model.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

public class RecipeMapper implements Mapper<Recipe, RecipeDto> {

    private static final String space = " ";

    private static final Logger logger = LoggerFactory.getLogger(RecipeMapper.class);

    @Override
    public RecipeDto fromEntityToDto(Recipe entity) {

      /*  var result = new recipeDto(entity.getId(), entity.getName() + space + entity.getCuisine(), entity.getSteps());*/
        var result = new RecipeDto(entity.getId(), entity.getName(), entity.getCuisine(), entity.getSteps(), entity.getIngredients());
        logger.debug("converting entity: [{}] to dto: [{}]", entity, result);
        return result;
    }

    @Override
    public Recipe fromDtoToEntity(RecipeDto dto) {

        var result = new Recipe(dto.getId(), dto.getName(), dto.getCuisine(), dto.getSteps(), dto.getIngredients());
        logger.debug("converting from dto: [{}] to enity: [{}]", dto, result);
        return result;
    }
}
