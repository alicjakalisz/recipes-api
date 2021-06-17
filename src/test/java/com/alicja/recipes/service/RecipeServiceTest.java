package com.alicja.recipes.service;

import com.alicja.recipes.converter.RecipeMapper;
import com.alicja.recipes.dto.RecipeDto;
import com.alicja.recipes.model.Recipe;
import com.alicja.recipes.respository.RecipeRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@SpringBootTest
@ContextConfiguration
class RecipeServiceTest {

   //create mock repository as we need it to test Service
    @Mock
    RecipeRepository recipeRepository;

   @InjectMocks
   RecipeService recipeService;


   @Autowired
   @Spy
   RecipeMapper mapper;

    @Test
    public void serviceShouldGetAllRecipesFromService() {
        List<RecipeDto> expectedResult = new ArrayList<>();
        expectedResult.add(new RecipeDto(123l, "pizza", "italian", "1232343", "flor,eggs, tomatoes"));
        expectedResult.add(new RecipeDto(124l, "pasta", "italian", "fgdf", "flor,eggs, olive"));

        List<Recipe> list = new ArrayList<>();
        list.add(new Recipe(123l, "pizza", "italian", "1232343", "flor,eggs, tomatoes"));
        list.add(new Recipe(124l, "pasta", "italian", "fgdf", "flor,eggs, olive"));

        when(recipeRepository.findAll()).thenReturn(list);

        List<RecipeDto> result = recipeService.findAllRecipes();

        assertEquals(expectedResult,result);
    }

    @Test
    public void serviceShouldGetRecipeById() {
        RecipeDto expectedResult = new RecipeDto(1L, "pizza", "italian", "1232343", "flor,eggs, tomatoes");

        //Mock Repository
        Recipe recipe = new Recipe(1L, "pizza", "italian", "1232343", "flor,eggs, tomatoes");
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        //method being tested
        //service uses findRecipeById method that calls repository, repository is replaced with mock that returns recipe object and
        //then still in the services method it is converted with a mapper to dtorecipe
        RecipeDto result = recipeService.findRecipeById(1L);
        assertEquals(expectedResult,result);
    }

    @Test
    public void serviceShouldReplaceRecipeWithNewOne() {

        //input for test method
        RecipeDto toReplaceOld = new RecipeDto(null, "pasta", "spanish", "1232343", "flor,eggs,olives, cheese");
        //mock return - save
        Recipe newRecipe = new Recipe(1l, "pasta", "spanish", "1232343", "flor,eggs,olives, cheese");
        //mock return - findId
        Recipe oldRecipe = new Recipe(1l, "pizza", "italian", "1232343", "flor,eggs, tomatoes");
        //expected result from the service
        RecipeDto expected = new RecipeDto(1l, "pasta", "spanish", "1232343", "flor,eggs,olives, cheese");
        //to mock
        when(recipeRepository.findById(1l)).thenReturn(Optional.of(oldRecipe));
        when(recipeRepository.save((any()))).thenReturn(newRecipe);

        //method tested
        RecipeDto result = recipeService.replaceRecipe(1l,toReplaceOld);

        //assert
        assertEquals(expected,result);

    }

    @Test
    public void serviceShouldDeleteRecipeById() {
        //method test
       boolean result = recipeService.deleteRecipeById(1l);

        //assert
        assertTrue(result);

    }
    @Test
    public void serviceShouldPartlyUpdateRecipeOfId() {

        //input for service method:
        RecipeDto recipeDto = new RecipeDto(null, "pasta", "spanish", "1232343", "flor,eggs,olives, cheese");
        //mock return - findById
        Recipe recipe= new Recipe(1l, "pizza", "italian", "1232343", "flor,eggs, tomatoes");
        // expected result from the method:
        RecipeDto expected = new RecipeDto(1l, "pasta", "spanish", "1232343", "flor,eggs, tomatoes");

        //mocks:
        when(recipeRepository.findById(1l)).thenReturn(Optional.of(recipe));

        //test
        RecipeDto result = recipeService.updateRecipeWithAttributes(1l,recipeDto);

        //assert
        assertEquals(expected,result);

    }
    @Test
    public void serviceShouldAddNewRecipe() {
        RecipeDto newRecipe = new RecipeDto(null, "pizza", "italian", "1232343", "flor,eggs, tomatoes");
        RecipeDto expectedRecipeSaved = new RecipeDto(1L, "pizza", "italian", "1232343", "flor,eggs, tomatoes");

        //These 2 lines are mocking the call to repository save
        Recipe recipeSaved = new Recipe(1L, "pizza", "italian", "1232343", "flor,eggs, tomatoes");
        when(recipeRepository.save(any())).thenReturn(recipeSaved);

        RecipeDto result = recipeService.saveRecipe(newRecipe);

        assertEquals(result,expectedRecipeSaved);
    }




}