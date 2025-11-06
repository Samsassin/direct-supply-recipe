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

/**
 * Service class for managing and retrieving recipes, including their instructions.
 * Provides functionality to retrieve a list of available recipes, fetch specific recipes
 * by name, and generate step-by-step instructions for a recipe by leveraging a Gemini model.
 */
@Slf4j
@Service
public class RecipeService {

    private final List<Recipe> recipes;
    private final String geminiDefaultModel;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InstructionPromptBuilder instructionPromptBuilder;

    /**
     * Constructs a RecipeService instance.
     *
     * @param recipes the list of available Recipe objects to be managed by this service
     * @param geminiDefaultModel the default model identifier for the Gemini model; injected via configuration
     * @param instructionPromptBuilder the builder responsible for creating instruction-generation prompts
     */
    public RecipeService(List<Recipe> recipes,
                         @Value("${gemini.default.model}") String geminiDefaultModel,
                         InstructionPromptBuilder instructionPromptBuilder) {
        this.recipes = recipes;
        this.geminiDefaultModel = geminiDefaultModel;
        this.instructionPromptBuilder = instructionPromptBuilder;
    }

    /**
     * Retrieves the list of available recipes managed by the service.
     *
     * @return a list of {@code Recipe} objects representing the available recipes
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Retrieves a recipe by its name from the list of available recipes.
     * If no recipe with the given name exists, this method returns {@code null}.
     *
     * @param name the name of the recipe to retrieve; case-insensitive
     * @return the {@code Recipe} object corresponding to the specified name, or {@code null} if not found
     */
    public Recipe getRecipe(String name) {
        return recipes.stream()
                .filter(recipe -> recipe.getTitle().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the instructions for a recipe specified by its name.
     * If no recipe with the given name exists, an empty list is returned.
     *
     * @param name the name of the recipe for which instructions are to be retrieved
     * @return a list of strings representing the instructions for the specified recipe,
     *         or an empty list if no such recipe is found
     */
    public List<String> getRecipeInstructions(String name) {
        Recipe recipe = getRecipe(name);
        if (recipe == null) {
            // Return an empty array when a recipe is not found to ensure a valid JSON array response
            return Collections.emptyList();
        }

        String promptText = instructionPromptBuilder.build(recipe);
        String rawResponse = generateInstructionResponse(promptText);
        log.info("Gemini response: {}", rawResponse);
        //TODO: [improvement] use Gemini's structured output to make things simpler.
        return parseInstructionSteps(rawResponse);
    }


    /**
     * Generates a response string containing instructions based on the given prompt text by invoking the Gemini model.
     *
     * @param promptText the input text prompt used to generate the instruction response
     * @return the generated instruction response as a string
     */
    private String generateInstructionResponse(String promptText) {
        Client client = new Client();
        GenerateContentResponse response = client.models.generateContent(geminiDefaultModel, promptText, null);
        return response.text();
    }

    /**
     * Parses a raw string containing instruction steps in JSON array format into a list of strings.
     * If the input cannot be parsed as a valid JSON array, an empty list is returned.
     *
     * @param rawText the raw instruction text to be parsed, expected to be in JSON array format
     * @return a list of strings representing individual instruction steps, or an empty list if parsing fails
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
