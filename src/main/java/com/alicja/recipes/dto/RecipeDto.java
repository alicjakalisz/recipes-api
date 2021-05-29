package com.alicja.recipes.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.persistence.*;
import java.util.Objects;


public class RecipeDto {


        private Long id;

        private String name;


        private String cuisine;


        private String steps;


        private String ingredients;

        public RecipeDto(Long id, String name, String cuisine, String steps, String ingredients) {
            this.id = id;
            this.name = name;
            this.cuisine = cuisine;
            this.steps = steps;
            this.ingredients = ingredients;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCuisine() {
            return cuisine;
        }

        public void setCuisine(String cuisine) {
            this.cuisine = cuisine;
        }

        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getIngredients() {
            return ingredients;
        }

        public void setIngredients(String  ingredients) {
            this.ingredients = ingredients;
        }

        @Override
        public String toString() {
            return "RecipeDto{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", cuisine='" + cuisine + '\'' +
                    ", steps='" + steps + '\'' +
                    ", ingredients=" + ingredients +
                    '}';
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDto recipeDto = (RecipeDto) o;
        return Objects.equals(id, recipeDto.id) && Objects.equals(name, recipeDto.name) && Objects.equals(cuisine, recipeDto.cuisine) && Objects.equals(steps, recipeDto.steps) && Objects.equals(ingredients, recipeDto.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cuisine, steps, ingredients);
    }
}
