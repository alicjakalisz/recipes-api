package com.alicja.recipes.respository;

import com.alicja.recipes.model.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query("from recipes ")
    List<Recipe> findAllRecipes();
}
