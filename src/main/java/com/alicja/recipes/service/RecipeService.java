package com.alicja.recipes.service;

import com.alicja.recipes.converter.RecipeMapper;
import com.alicja.recipes.dto.RecipeDto;
import com.alicja.recipes.exception.RecipeNotFoundException;
import com.alicja.recipes.model.Recipe;
import com.alicja.recipes.respository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class RecipeService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);


    private RecipeRepository recipeRepository;

    private RecipeMapper recipeMapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    public RecipeService(){

    }

    public List<RecipeDto> findAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        recipeRepository.findAll().iterator().forEachRemaining(list::add);

        var result = list.stream()
                .map(recipe -> recipeMapper.fromEntityToDto(recipe))
                .collect(toList());

        logger.info("number of found books: [{}]", result.size());
        logger.debug("result: {}", result);

        return result;
    }

    public RecipeDto findRecipeById(Long id) {
        Objects.requireNonNull(id, "id parameter mustn't be null!!!");

        var result = recipeMapper.fromEntityToDto(findRecipeByIdFromRepository(id));
        logger.info("book found for id: [{}] is: [{}]", id, result);

        return result;
    }

/*    private Recipe findRecipeByIdFromRepository(Long id) {
        return recipeRepository.findAllRecipes()
                .stream()
                .filter(recipe -> recipe.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecipeNotFoundException(String.format("No recipe with id:[%d]", id)));
    }*/

    private Recipe findRecipeByIdFromRepository(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(String.format("No recipe with id:[%d]", id)));
    }
    public RecipeDto saveRecipe(RecipeDto toSave) {

        // find max id
        // add recipe with id (max id + 1)
        // return book with id
       /* Long currentMaxId = recipeRepository.findAllRecipes()
                .stream()
                .mapToLong(value -> value.getId())
                .max()
                .orElse(1);*/
        Recipe entityToSave = recipeMapper.fromDtoToEntity(toSave);
       // entityToSave.setId(currentMaxId + 1)

        Recipe result = recipeRepository.save(entityToSave);
        logger.info("saved recipe: [{}]", entityToSave);

        return recipeMapper.fromEntityToDto(result);
    }

    @Transactional
    public boolean deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
        logger.info("trying to delete book with id: [{}]", id);
        return true;
    }

    // Transactional
    public RecipeDto replaceRecipe(Long id, RecipeDto toReplace) {
        Recipe recipe = findRecipeByIdFromRepository(id);

        Recipe recipeMapped = recipeMapper.fromDtoToEntity(toReplace);
        recipeMapped.setId(recipe.getId());
        logger.info("replacing book [{}] with new one [{}]", recipe, toReplace);
        recipeRepository.save(recipeMapped);
        return recipeMapper.fromEntityToDto(recipeMapped);
    }

    public RecipeDto updateRecipeWithAttributes(Long id, RecipeDto toUpdate) {

        Recipe recipeEntityToUpdate = recipeMapper.fromDtoToEntity(toUpdate);

        Recipe recipe = findRecipeByIdFromRepository(id);

        if (Objects.nonNull(recipeEntityToUpdate.getName())) {
            recipe.setName(recipeEntityToUpdate.getName());
        }

      //  TODO different attributes to be changed
        if (Objects.nonNull(recipeEntityToUpdate.getCuisine())) {
            recipe.setCuisine(recipeEntityToUpdate.getCuisine());
        }

        recipeRepository.save(recipe);

        logger.info("updated recipe: [{}], with changes to apply: [{}]", recipe, recipeEntityToUpdate);
        return recipeMapper.fromEntityToDto(recipe);
    }
}

