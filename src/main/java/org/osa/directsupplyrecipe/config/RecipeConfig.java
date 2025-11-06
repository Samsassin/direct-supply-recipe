package org.osa.directsupplyrecipe.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.osa.directsupplyrecipe.model.Recipe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Configuration class for loading and managing recipe data.
 *
 * This class is responsible for loading recipe data from an external JSON file located in the classpath.
 * It uses the provided ResourceLoader to access the file and the ObjectMapper to parse the JSON into
 * a list of Recipe objects. The recipes are returned as a Spring Bean.
 *
 * This configuration ensures that the application has access to a predefined list of recipes by
 * loading and parsing data during application startup.
 *
 * Dependencies:
 * - ResourceLoader: Used to locate and read the external JSON file.
 * - ObjectMapper: Used to parse the JSON content into Java objects.
 *
 * Methods:
 * - getRecipes(): Loads recipes from the JSON file, parses them into Recipe objects, and returns a list of recipes.
 *
 * Logs the loading process to provide visibility during execution.
 *
 * Throws:
 * - IOException: If an error occurs while reading the JSON file.
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class RecipeConfig {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    /**
     * Loads and returns a list of recipes from a JSON file located in the classpath.
     *
     * This method uses the ResourceLoader to read the JSON file and the ObjectMapper
     * to parse the contents into a list of Recipe objects. The JSON file must be
     * located under the classpath with the name "recipes.json". Logs the process
     * during execution for monitoring.
     *
     * @return a list of Recipe objects parsed from the JSON file
     * @throws IOException if an error occurs while reading or parsing the JSON file
     */
    @Bean
    public List<Recipe> getRecipes() throws IOException {
        log.info("Loading recipes");
        Resource resource = resourceLoader.getResource("classpath:recipes.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        }
    }

}
