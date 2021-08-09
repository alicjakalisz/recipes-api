package com.alicja.recipes.controller;

import com.alicja.recipes.dto.RecipeDto;
import com.alicja.recipes.model.Recipe;
import com.alicja.recipes.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class RecipeController {
    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    //REST methods
    @GetMapping("recipes")
    public List<RecipeDto> getAllRecipes() {

        logger.info("getAllRecipes()");

        return recipeService.findAllRecipes();
    }




    @GetMapping("recipes/{id}")
    public RecipeDto getRecipeById(@PathVariable("id") Long id) {
        logger.info("find recipe by id: [{}]", id);

        //return RecipeDto
       return recipeService.findRecipeById(id);


    }

    @PostMapping("recipes")
    //r????
    public ResponseEntity<RecipeDto> addRecipe(@RequestBody RecipeDto toSave) {
        logger.info("adding recipe: [{}]", toSave);

        var newRecipe = recipeService.saveRecipe(toSave);
        //TODO
        return ResponseEntity.created(URI.create("recipes/" + newRecipe.getId()))
                .body(newRecipe);
    }

    @DeleteMapping("recipes/{id}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable("id") Long id) {
        logger.info("deleting recipe by id: [{}]", id);
        // delete true - RC 204
        // delete false - RC 4xx
        boolean deleted = recipeService.deleteRecipeById(id);

        ResponseEntity<Void> result = ResponseEntity.notFound().build();
        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return result;

//        return deleted ?  ResponseEntity.noContent().build() : ResponseEntity.notFound().build() ;
    }

    // update (replace totally)
    @PutMapping("recipes/{id}")
    public RecipeDto replaceRecipe(@PathVariable("id") Long id, @RequestBody RecipeDto toReplace) {
        logger.info("replacing recipe with id: [{}] with new one: [{}]", id, toReplace);
        return recipeService.replaceRecipe(id, toReplace);
    }

    // update (partial - only some attributes)
    @PatchMapping("recipes/{id}")
    public RecipeDto updateBook(@PathVariable("id") Long id, @RequestBody RecipeDto toUpdate) {
        logger.info("updating book with id: [{}] with new attributes: [{}]", id, toUpdate);
        return recipeService.updateRecipeWithAttributes(id, toUpdate);
    }
}
