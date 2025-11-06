import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RecipeService, Recipe } from './recipe.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private api = inject(RecipeService);

  recipes = signal<Recipe[]>([]);
  recipesLoading = signal<boolean>(true);

  selected = signal<Recipe | null>(null);

  instructions = signal<string[]>([]);
  instructionsLoading = signal<boolean>(false);

  /**
   * Lifecycle constructor invoked when the component is created.
   * Triggers the initial load of recipes from the backend.
   */
  constructor() {
    this.loadRecipes();
  }

  /**
   * Fetches the list of recipes from the API service and updates reactive signals.
   * - Sets recipesLoading=true while request is in-flight.
   * - On success: populates recipes and marks loading=false.
   * - On error: clears recipes and marks loading=false to unblock the UI.
   */
  loadRecipes() {
    this.recipesLoading.set(true);
    this.api.getRecipes().subscribe({
      next: data => {
        this.recipes.set(data);
        this.recipesLoading.set(false);
      },
      error: _ => {
        this.recipes.set([]);
        this.recipesLoading.set(false);
      }
    });
  }

  /**
   * Selects a recipe from the list and loads its instructions.
   * @param r Recipe to select
   */
  select(r: Recipe) {
    this.selected.set(r);
    this.fetchInstructions(r.title);
  }

  /**
   * Re-fetches instructions for the currently selected recipe (if any).
   * Safe to call when nothing is selected; it will no-op.
   */
  refreshInstructions() {
    const r = this.selected();
    if (r) this.fetchInstructions(r.title);
  }

  /**
   * Internal helper to fetch step-by-step instructions for a given recipe name.
   * Handles loading state and error fallback (clears instructions on error).
   * @param name Recipe title to query for instructions
   */
  private fetchInstructions(name: string) {
    this.instructionsLoading.set(true);
    this.instructions.set([]);
    this.api.getInstructions(name).subscribe({
      next: steps => {
        this.instructions.set(steps);
        this.instructionsLoading.set(false);
      },
      error: _ => {
        this.instructions.set([]);
        this.instructionsLoading.set(false);
      }
    });
  }
}
