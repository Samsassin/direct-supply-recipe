package org.osa.directsupplyrecipe.model;

import lombok.Data;

import java.util.List;

/**
 * Represents a recipe with a title, yield, ingredients, and step-by-step instructions.
 * This class provides the basic structure for storing and managing recipe information.
 */
@Data
public class Recipe {
    private String title;
    private int yield;
    private List<String> ingredients;
}
