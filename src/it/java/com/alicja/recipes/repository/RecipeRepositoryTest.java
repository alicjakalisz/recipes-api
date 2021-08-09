package com.alicja.recipes.repository;

import com.alicja.recipes.model.Recipe;
import com.alicja.recipes.respository.RecipeRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
public class RecipeRepositoryTest {

    @Autowired
    RecipeRepository repository;

    @Test
    public void shouldAddRecipeInRepository(){
        Recipe recipe = new Recipe();
        recipe.setCuisine("fdsafds");
        repository.save(recipe);
    }
}
