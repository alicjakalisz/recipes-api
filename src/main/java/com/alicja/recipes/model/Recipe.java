package com.alicja.recipes.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "recipes")
//@Table(name = "RECIPES")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column
    private String cuisine;

    @Column(name = "steps")
    private String steps;

    @Column
    private String ingredients;

    public Recipe(Long id, String name, String cuisine, String steps, String ingredients) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    public Recipe() {
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
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", steps='" + steps + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
