package org.osa.directsupplyrecipe.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.osa.directsupplyrecipe.model.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.osa.directsupplyrecipe.utils.JsonArraySanitizer;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RecipeService {

    private final List<Recipe> recipes;
    private final String geminiDefaultModel;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InstructionPromptBuilder instructionPromptBuilder;

    public RecipeService(List<Recipe> recipes,
                         @Value("${gemini.default.model}") String geminiDefaultModel,
                         InstructionPromptBuilder instructionPromptBuilder) {
        this.recipes = recipes;
        this.geminiDefaultModel = geminiDefaultModel;
        this.instructionPromptBuilder = instructionPromptBuilder;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Recipe getRecipe(String name) {
        return recipes.stream()
                .filter(recipe -> recipe.getTitle().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<String> getRecipeInstructions(String name) {
        Recipe recipe = getRecipe(name);
        if (recipe == null) {
            // Return an empty array when a recipe is not found to ensure a valid JSON array response
            return Collections.emptyList();
        }

        String promptText = instructionPromptBuilder.build(recipe);
        String rawResponse = generateInstructionResponse(promptText);
        log.info("Gemini response: {}", rawResponse);
        return parseInstructionSteps(rawResponse);
    }


    /**
     * Invoke the Gemini model and return the raw text response.
     */
    private String generateInstructionResponse(String promptText) {
        Client client = new Client();
        GenerateContentResponse response = client.models.generateContent(geminiDefaultModel, promptText, null);
        return response.text();
    }

    /**
     * Parse the model output into a list of instruction steps; never returns null.
     */
    private List<String> parseInstructionSteps(String rawText) {
        try {
            String cleaned = JsonArraySanitizer.sanitizeToJsonArray(rawText);
            List<String> steps = objectMapper.readValue(cleaned, new TypeReference<>() {});
            return steps == null ? Collections.emptyList() : steps;
        } catch (Exception e) {
            log.warn("Failed to parse instructions as JSON array. Returning empty array. Error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

}
