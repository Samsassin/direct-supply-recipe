package org.osa.directsupplyrecipe.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.osa.directsupplyrecipe.model.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RecipeService {

    private final List<Recipe> recipes;
    private final String geminiDefaultModel;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String EMPTY_JSON_ARRAY = "[]";

    public RecipeService(List<Recipe> recipes, @Value("${gemini.default.model}") String geminiDefaultModel) {
        this.recipes = recipes;
        this.geminiDefaultModel = geminiDefaultModel;
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

        String promptText = buildInstructionPrompt(recipe);
        String rawResponse = generateInstructionResponse(promptText);
        log.info("Gemini response: {}", rawResponse);
        return parseInstructionSteps(rawResponse);
    }

    /**
     * Build a clear, constrained prompt for the model based on the given recipe.
     */
    private String buildInstructionPrompt(Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are an expert chef.\n");
        sb.append("Create clear, complete cooking instructions for the recipe below.\n");
        sb.append("Return ONLY a valid JSON array of strings.\n");
        sb.append("Each array element must be one step.\n");
        sb.append("Do not include numbers, titles, commentary, code fences, or any text outside the JSON array.\n");
        sb.append("Order steps from first to last.\n\n");

        sb.append("Recipe title: ")
          .append(recipe.getTitle() == null ? "" : recipe.getTitle())
          .append("\n");
        sb.append("Yield: ")
          .append(recipe.getYield())
          .append("\n");
        sb.append("Ingredients:\n");
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            for (String ing : recipe.getIngredients()) {
                sb.append("- ").append(ing).append("\n");
            }
        } else {
            sb.append("- (none provided)\n");
        }
        return sb.toString();
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
            String cleaned = sanitizeToJsonArray(rawText);
            List<String> steps = objectMapper.readValue(cleaned, new TypeReference<>() {});
            return steps == null ? Collections.emptyList() : steps;
        } catch (Exception e) {
            log.warn("Failed to parse instructions as JSON array. Returning empty array. Error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String sanitizeToJsonArray(String inputText) {
        if (inputText == null) return EMPTY_JSON_ARRAY;

        String trimmed = inputText.trim();
        String withoutFences = stripWrappingCodeFences(trimmed);
        String arrayOnly = extractFirstBracketedArray(withoutFences);

        if (!looksLikeJsonArray(arrayOnly)) {
            return EMPTY_JSON_ARRAY;
        }
        return arrayOnly;
    }

    private String stripWrappingCodeFences(String text) {
        if (text.length() >= 6 && text.startsWith("```") && text.endsWith("```")) {
            return text.substring(3, text.length() - 3).trim();
        }
        return text;
    }

    private String extractFirstBracketedArray(String text) {
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1).trim();
        }
        return text.trim();
    }

    private boolean looksLikeJsonArray(String text) {
        return text != null && text.startsWith("[") && text.endsWith("]");
    }
}
