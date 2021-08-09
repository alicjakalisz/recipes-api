package com.alicja.recipes.respository;

import com.alicja.recipes.dto.RecipeDto;
import com.alicja.recipes.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration
class RecipeRepositoryTest2 {

    @Autowired
    RecipeRepository repository;

    @Test
    public void shouldAddRecipeInRepository(){
        Recipe recipe = new Recipe();
        recipe.setCuisine("fdsafds");
        repository.save(recipe);
    }

}