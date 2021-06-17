package com.alicja.recipes.controller;

import com.alicja.recipes.dto.RecipeDto;
import com.alicja.recipes.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {
    //mocking the tom cat server Mvc - model view controller as Spring app is not run.
    @Autowired
    private MockMvc mockMvc;

    //we are mocking the service object as we need it for controller testing
    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void controllerShouldGetAllRecipesFromService() throws Exception {

        List<RecipeDto> list = new ArrayList<>();
        list.add(new RecipeDto(123l, "pizza", "italian", "1232343", "flor,eggs, tomatoes"));
        list.add(new RecipeDto(124l, "pasta", "italian", "fgdf", "flor,eggs, olive"));

        when(recipeService.findAllRecipes()).thenReturn(list);

        ResultActions resultActions = mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(123)))
                .andExpect(jsonPath("$[0].name", is("pizza")))
                .andExpect(jsonPath("$[1].id", is(124)))
                .andExpect(jsonPath("$[1].name", is("pasta")));

        MvcResult result = resultActions.andReturn();
        String contentString = result.getResponse().getContentAsString();
        System.out.println(contentString);
        List<RecipeDto> outcome = objectMapper.readValue(contentString, new TypeReference<List<RecipeDto>>() {
        });
        assertEquals(outcome, list);

        //.andExpect(content().json("{\"a\":\"1\"}"));
    }

    @Test
    public void controllerShouldGetRecipeById() throws Exception {
        List<RecipeDto> list = new ArrayList<>();
        list.add(new RecipeDto(123l, "pizza", "italian", "1232343", "flor,eggs, tomatoes"));
        list.add(new RecipeDto(124l, "pasta", "italian", "fgdf", "flor,eggs, olive"));

        when(recipeService.findRecipeById(123l)).thenReturn(list.get(0));

        this.mockMvc.perform(get("/recipes/123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id", is(123)));

    }

    @Test
    public void controllerShouldReplaceRecipeWithNewOne() throws Exception {
        //mock input:
        RecipeDto toReplaceTheOld = new RecipeDto(null, "paella", "spanish", "fgdf", "flor,eggs, olive");

        //mock return:
        RecipeDto newOne = new RecipeDto(1l, "paella", "spanish", "fgdf", "flor,eggs, olive");

        //mock
        when(recipeService.replaceRecipe(1l,toReplaceTheOld)).thenReturn(newOne);
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
    }

    @Test
    public void controllerShouldDeleteRecipeById() throws Exception{
        //mocks
        when(recipeService.deleteRecipeById(1l)).thenReturn(true);

        //tested:
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void controllerShouldPartlyUpdateRecipeOfId() throws Exception {
        RecipeDto toReplaceTheOld = new RecipeDto(null, "paella", "spanish", "fgdf", "flor,eggs, olive");
        RecipeDto newOne = new RecipeDto(1l, "paella", "spanish", "fgdf", "flor,eggs, olive");

        //mock
        when(recipeService.updateRecipeWithAttributes(1L,toReplaceTheOld)).thenReturn(newOne);

        mockMvc.perform(patch("/recipes/1")
                .content(asJsonString(toReplaceTheOld))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.name",is("paella")))
                .andExpect(jsonPath("$.cuisine",is("spanish")));

    }

    @Test
    public void controllerShouldAddNewRecipe() throws Exception {
        RecipeDto recipeDto = new RecipeDto(null, "pasta", "italian", "fgdf", "flor,eggs, olive");
        RecipeDto expected = new RecipeDto(1L, "pasta", "italian", "fgdf", "flor,eggs, olive");
        when(recipeService.saveRecipe(recipeDto)).thenReturn(expected);

        mockMvc.perform(post("/recipes")
                .content(asJsonString(recipeDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id",is(1)));


    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}