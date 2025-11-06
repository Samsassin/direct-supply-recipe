package org.osa.directsupplyrecipe.service;

import org.osa.directsupplyrecipe.model.Recipe;
import org.springframework.stereotype.Component;

/**
 * Builds the instruction-generation prompt text for a given recipe.
 */
@Component
public class InstructionPromptBuilder {

    /**
     * Build a clear, constrained prompt for the model based on the given recipe.
     */
    public String build(Recipe recipe) {
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
}
