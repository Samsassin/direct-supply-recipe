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

@Slf4j
@AllArgsConstructor
@Configuration
public class RecipeConfig {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Bean
    public List<Recipe> getRecipes() throws IOException {
        log.info("Loading recipes");
        Resource resource = resourceLoader.getResource("classpath:recipes.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        }
    }

}
