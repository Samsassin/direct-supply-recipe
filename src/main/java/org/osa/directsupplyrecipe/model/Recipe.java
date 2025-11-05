package org.osa.directsupplyrecipe.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Recipe {
    private String title;
    private int yield;
    private List<String> ingredients;
    private List<String> instructions = new ArrayList<>();

}
