# Direct Supply: Recipe Challenge

This README explains how to run and test the app, lists assumptions, and documents the current API used by the UI.

## How to Run

### Quick start
- Start the backend (Spring Boot + Gradle):
  - Windows PowerShell/CMD: `gradlew.bat bootRun`
  - macOS/Linux: `./gradlew bootRun`
  - Backend URL: http://localhost:8080
- Start the frontend (Angular 18 + Angular Material):
  1) Open a terminal in `ui/`
  2) Install deps: `npm install`
  3) Run dev server: `npm start`
  - UI URL: http://localhost:4200
  - API calls from the UI to `/recipe` are proxied to the backend at http://localhost:8080

### Prerequisites
- Backend: Java 21, Gradle wrapper (included: `gradlew`/`gradlew.bat`)
- Frontend: Node.js 20+ and npm 10+

### Configuration
- Backend port defaults to 8080. You can change it in `src/main/resources/application.properties` using `server.port=XXXX`.
- The UI dev proxy routes `/recipe` to the backend. If you change backend ports, update the UI proxy config accordingly (see `ui` scripts/config).

### Notes
- Ensure the Spring Boot backend is running so the UI can fetch data.
- The UI provides:
  - A list of recipes loaded from GET `/recipe`
  - A details view with ingredients
  - Generated instructions from GET `/recipe/{title}/instructions`

## Build
- Backend (assemble JAR and run tests):
  - Windows: `gradlew.bat build`
  - macOS/Linux: `./gradlew build`
  - Output: `build/libs/` (e.g., `direct-supply-recipe-<version>.jar`)
- Frontend (production build):
  - From `ui/`: `npm run build`
  - Output: `ui/dist/`

## Challenge Overview
Build a minimal web application with a preconfigured front end and back end. Do not spend time on polish or tests.

### Required Pages
- Recipes list page: Lists available recipes stored in the back end; clicking a recipe navigates to that recipe’s page.
- Recipe detail page: Shows the ingredients for a selected recipe. No interactive elements are required on this page.

### Technology
- Spring Boot + Gradle for the backend.
- In-memory data store (Java collections) instead of a full database.
- Optional: access to LLM models (completions, embeddings) via an OpenAI‑compatible server; quality is not important for the interview.

## Provided Meal Plan Data
```json
[
  {
    "title": "Chicken Stir-Fry",
    "yield": 2,
    "ingredients": [
      "30g olive oil",
      "200g chicken breast",
      "500g broccoli florets",
      "250g red bell pepper",
      "60g soy sauce",
      "10g garlic",
      "5g ginger"
    ]
  },
  {
    "title": "Veggie Omelette",
    "yield": 1,
    "ingredients": [
      "100g large eggs",
      "40g diced onion",
      "50g diced tomato",
      "20g spinach leaves",
      "15g olive oil",
      "150g small corn tortillas",
      "3g Salt and pepper (to taste)"
    ]
  },
  {
    "title": "Overnight Oats",
    "yield": 4,
    "ingredients": [
      "160g rolled oats",
      "1000g almond milk",
      "70g chia seeds",
      "60g honey",
      "300g blueberries"
    ]
  }
]
```

## Assumptions & Clarifications
- Routing: simple two-page flow — list → details. No create/edit/delete.
- Data source: in-memory storage initialized at startup from the provided JSON payload. No persistence across restarts.
- Units: ingredient quantities are treated as display text (no unit parsing).
- IDs/slugs: titles are assumed unique; title can identify a recipe.
- Error cases: minimal handling (e.g., 404 if a recipe is not found). No auth.
- Front end: minimal UI with Angular + Material; no styling requirements beyond defaults.
- LLM usage: optional and behind configuration.
- Performance: tiny dataset; no special performance work.

## API Reference (current)
An OpenAPI 3.1 specification is available at ./docs/openapi.yaml.
Endpoints used by the UI and provided by the backend:
- GET `/recipe` — returns an array of recipes with at least `title` and `yield`.
- GET `/recipe/{title}` — returns the full recipe, including `ingredients`.
- GET `/recipe/{title}/instructions` — returns generated instructions for the specified recipe.

Note: Previous drafts mentioned `/api/recipes` endpoints; those were conceptual. The current implementation and UI use `/recipe`.

## How to Test (lightweight)
- Manual verification via browser, `curl`, or Postman is sufficient per the prompt.
- If you add tests: `gradlew.bat test` (or `./gradlew test` on macOS/Linux).

## Optional: LLM Integration Notes
If you choose to integrate LLM calls:
- Provide two client methods: `complete(prompt)` and `embed(texts[])` against an OpenAI‑compatible base URL and API key.
- Make base URL and key configurable via environment variables or `application.properties`.
- Keep the feature optional; fall back to non‑LLM behavior if not configured.

## Future Enhancements (out of scope for now)
- Persist recipes in a real database.
- Add create/edit/delete UI.
- Improve error handling, input validation, and accessibility.
- Add automated tests and CI.




