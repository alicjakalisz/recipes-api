package com.alicja.recipes;

import com.alicja.recipes.controller.RecipeController;
import com.alicja.recipes.dto.RecipeDto;
import com.alicja.recipes.model.Recipe;
import com.alicja.recipes.respository.RecipeRepository;
import com.alicja.recipes.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecipesAppIntegrationTest {
    //mocking the tom cat server Mvc - model view controller as Spring app is not run.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;


    @Test
    @Order(1)
    public void controllerShouldGetZeroRecipesWhenDataBaseIsEmpty() throws Exception {
        List<RecipeDto> list = new ArrayList<>();

        ResultActions resultActions = mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));

        MvcResult result = resultActions.andReturn();
        String contentString = result.getResponse().getContentAsString();
        System.out.println(contentString);
        List<RecipeDto> outcome = objectMapper.readValue(contentString, new TypeReference<List<RecipeDto>>() {
        });
        assertEquals(outcome, list);
    }

    @Test
    @Order(2)
    public void controllerShouldAddNewRecipe() throws Exception {
        RecipeDto recipeDto = new RecipeDto(null, "pasta", "italian", "fgdf", "flor,eggs, olive");
        mockMvc.perform(post("/recipes")
                .content(asJsonString(recipeDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id",is(1)));
        List<Recipe> recipeList = new ArrayList<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeList::add);
        assertEquals(recipeList.size(),1);

    }

    @Test
    @Order(3)
    public void controllerShouldGetRecipeById() throws Exception {
        RecipeDto expected = new RecipeDto(1L, "pasta", "italian", "fgdf", "flor,eggs, olive");

        ResultActions resultActions = this.mockMvc.perform(get("/recipes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id", is(1)));

        MvcResult result = resultActions.andReturn();
        String contentString = result.getResponse().getContentAsString();
        RecipeDto outcome = objectMapper.readValue(contentString, new TypeReference<RecipeDto>() {});
        assertEquals(outcome, expected);
    }


    @Test
    @Order(4)
    public void controllerShouldGetAllRecipes() throws Exception {
        List<RecipeDto> expected = new ArrayList<>();
        expected.add(new RecipeDto(1L, "pasta", "italian", "fgdf", "flor,eggs, olive"));
        ResultActions resultActions = mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("pasta")));
        MvcResult result = resultActions.andReturn();
        String contentString = result.getResponse().getContentAsString();
        List<RecipeDto> outcome = objectMapper.readValue(contentString, new TypeReference<List<RecipeDto>>() {});
        assertEquals(outcome, expected);
    }

    @Test
    @Order(5)
    public void controllerShouldReplaceRecipeWithNewOne() throws Exception {
        //mock input:
        RecipeDto toReplaceTheOld = new RecipeDto(null, "paella", "spanish", "fgdf", "flor,eggs, olive");
        //tested
        this.mockMvc.perform(MockMvcRequestBuilders.put("/recipes/1")
                .content(asJsonString(toReplaceTheOld))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.name",is("paella")))
                .andExpect(jsonPath("$.cuisine",is("spanish")));

        assertEquals(recipeRepository.findById(1L).get().getName(),"paella");
    }

    @Test
    @Order(6)
    public void controllerShouldPartlyUpdateRecipeOfId() throws Exception {
        RecipeDto toReplaceTheOld = new RecipeDto(null, "pierogi", "polish", "fgdf", "flor,eggs, olive");

        mockMvc.perform(patch("/recipes/1")
                .content(asJsonString(toReplaceTheOld))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.name",is("pierogi")))
                .andExpect(jsonPath("$.cuisine",is("polish")));

        assertEquals(recipeRepository.findById(1L).get().getName(),"pierogi");
    }

    @Test
    @Order(7)
    public void controllerShouldDeleteRecipeById() throws Exception{
        //tested:
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1"))
                .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));


    }





    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}