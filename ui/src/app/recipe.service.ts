import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Recipe {
  title: string;
  yield: number;
  ingredients: string[];
}

@Injectable({ providedIn: 'root' })
export class RecipeService {
  private http = inject(HttpClient);

  getRecipes(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(`/recipe`);
  }

  getRecipe(name: string): Observable<Recipe> {
    const encoded = encodeURIComponent(name);
    return this.http.get<Recipe>(`/recipe/${encoded}`);
  }

  getInstructions(name: string): Observable<string[]> {
    const encoded = encodeURIComponent(name);
    return this.http.get<string[]>(`/recipe/${encoded}/instructions`);
  }
}
