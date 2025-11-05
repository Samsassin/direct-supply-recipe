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
  template: `
    <mat-toolbar color="primary">Direct Supply Recipes</mat-toolbar>

    <div class="container">
      <div class="list">
        <mat-card>
          <h2>Recipes</h2>
          <ng-container *ngIf="recipesLoading(); else recipesList">
            <div class="center"><mat-progress-spinner mode="indeterminate" diameter="36"></mat-progress-spinner></div>
          </ng-container>
          <ng-template #recipesList>
            <mat-nav-list>
              <a mat-list-item *ngFor="let r of recipes()" (click)="select(r)" [class.active]="selected()?.title===r.title">
                {{ r.title }}
              </a>
            </mat-nav-list>
          </ng-template>
        </mat-card>
      </div>

      <div class="details">
        <mat-card *ngIf="selected(); else empty">
          <h2>{{ selected()?.title }}</h2>
          <p>Serves: {{ selected()?.yield }}</p>

          <h3>Ingredients</h3>
          <ul>
            <li *ngFor="let ing of selected()?.ingredients">{{ ing }}</li>
          </ul>

          <h3>Instructions</h3>
          <ng-container *ngIf="instructionsLoading(); else steps">
            <div class="center"><mat-progress-spinner mode="indeterminate" diameter="36"></mat-progress-spinner></div>
          </ng-container>
          <ng-template #steps>
            <ol>
              <li *ngFor="let s of instructions()">{{ s }}</li>
            </ol>
            <button mat-raised-button color="primary" (click)="refreshInstructions()">Regenerate</button>
          </ng-template>
        </mat-card>
        <ng-template #empty>
          <mat-card>
            <p>Select a recipe to view details.</p>
          </mat-card>
        </ng-template>
      </div>
    </div>
  `,
  styles: [`
    .container{display:flex;gap:16px;padding:16px;}
    .list{flex:1;min-width:260px;}
    .details{flex:2;}
    .center{display:flex;justify-content:center;align-items:center;padding:16px;}
    a.mat-list-item.active{background:#e3f2fd;}
    h2{margin-top:0}
  `]
})
export class AppComponent {
  private api = inject(RecipeService);

  recipes = signal<Recipe[]>([]);
  recipesLoading = signal<boolean>(true);

  selected = signal<Recipe | null>(null);

  instructions = signal<string[]>([]);
  instructionsLoading = signal<boolean>(false);

  constructor() {
    this.loadRecipes();
  }

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

  select(r: Recipe) {
    this.selected.set(r);
    this.fetchInstructions(r.title);
  }

  refreshInstructions() {
    const r = this.selected();
    if (r) this.fetchInstructions(r.title);
  }

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
