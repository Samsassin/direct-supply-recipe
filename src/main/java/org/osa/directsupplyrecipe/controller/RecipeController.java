package org.osa.directsupplyrecipe.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.osa.directsupplyrecipe.model.Recipe;
import org.osa.directsupplyrecipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/recipe")
    public ResponseEntity<List<Recipe>> getRecipes() {
        log.info("Get all recipes");
        List<Recipe> recipes = recipeService.getRecipes();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/recipe/{recipeName}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable String recipeName) {
        log.info("Get recipe: {}", recipeName);
        Recipe recipe = recipeService.getRecipe(recipeName);
        ResponseEntity<Recipe> response = ResponseEntity.ok(recipe);
        return response;
    }

    @GetMapping("/recipe/{recipeName}/instructions")
    public ResponseEntity<List<String>> getRecipeInstructions(@PathVariable String recipeName) {
        log.info("Get recipe instructions for: {}", recipeName);
        List<String> instructions = recipeService.getRecipeInstructions(recipeName);
        return ResponseEntity.ok(instructions);
    }
}
