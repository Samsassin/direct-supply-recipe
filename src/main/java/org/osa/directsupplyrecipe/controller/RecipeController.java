package org.osa.directsupplyrecipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.osa.directsupplyrecipe.model.Recipe;
import org.osa.directsupplyrecipe.service.RecipeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller class for handling HTTP requests related to recipes.
 * Provides endpoints for retrieving all recipes, fetching specific recipes by name,
 * and retrieving instructions for specific recipes.
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Handles HTTP GET requests to retrieve a list of all available recipes.
     *
     * @return a {@code ResponseEntity} containing a list of {@code Recipe} objects representing all recipes
     */
    @Operation(summary = "Get all recipes")
    @ApiResponse(
            responseCode = "200",
            description = "Recipes retrieved successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Recipe.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<Recipe>> getRecipes() {
        log.info("Get all recipes");
        List<Recipe> recipes = recipeService.getRecipes();
        return ResponseEntity.ok(recipes);
    }

    /**
     * Handles HTTP GET requests to retrieve a specific recipe by its name.
     *
     * @param recipeName the name of the recipe to retrieve
     * @return a {@code ResponseEntity} containing the {@code Recipe} object corresponding to the specified name
     */
    @Operation(summary = "Get recipe by name")
    @ApiResponse(
            responseCode = "200",
            description = "Recipe retrieved successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Recipe.class)
            )
    )
    @GetMapping("/{recipeName}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable String recipeName) {
        log.info("Get recipe: {}", recipeName);
        Recipe recipe = recipeService.getRecipe(recipeName);
        return ResponseEntity.ok(recipe);
    }

    /**
     * Handles HTTP GET requests to retrieve the step-by-step instructions for a specific recipe by its name.
     *
     * @param recipeName the name of the recipe for which instructions are to be retrieved
     * @return a {@code ResponseEntity} containing a list of strings, each representing a step in the recipe's instructions
     */
    @Operation(summary = "Get recipe instructions by name")
    @ApiResponse(
            responseCode = "200",
            description = "Recipe instructions retrieved successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)
            )
    )
    @GetMapping("/{recipeName}/instructions")
    public ResponseEntity<List<String>> getRecipeInstructions(@PathVariable String recipeName) {
        log.info("Get recipe instructions for: {}", recipeName);
        List<String> instructions = recipeService.getRecipeInstructions(recipeName);
        return ResponseEntity.ok(instructions);
    }
}
