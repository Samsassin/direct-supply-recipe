package org.osa.directsupplyrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point for the DirectSupplyRecipeApplication.
 *
 * This class is responsible for bootstrapping and launching the
 * Spring Boot application. It uses the {@code SpringBootApplication}
 * annotation to enable auto-configuration, component scanning,
 * and other configuration settings for the application.
 *
 * The {@code main} method serves as the starting point of the
 * application, where the Spring framework initializes various
 * components and manages the lifecycle of the application context.
 */
@SpringBootApplication
public class DirectSupplyRecipeApplication {

    /**
     * The main method serves as the entry point of the application.
     *
     * This method initializes and starts the Spring Boot application using
     * the {@code SpringApplication.run()} method, which sets up the
     * application context and begins its execution lifecycle.
     *
     * @param args command-line arguments passed to the application,
     *             which can be used for configuration or processing purposes.
     */
    static void main(String[] args) {
        SpringApplication.run(DirectSupplyRecipeApplication.class, args);
    }

}
