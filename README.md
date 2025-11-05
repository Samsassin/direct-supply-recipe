# direct-supply-recipe

A simple interview coding challenge. This README organizes and clarifies the instructions, calls out assumptions, and explains how to run/test the app in this repository.

## Challenge Overview
Build a minimal web application with a preconfigured front end and back end. Do not spend time on polish or tests.

### Required Pages
- Recipes list page: Lists available recipes stored in the back end. Clicking a recipe navigates to that recipe’s page.
- Recipe detail page: Shows the ingredients for a selected recipe. No interactive elements are required on this page.

### Technology
- "Use whatever frameworks you like." Spring Boot + Gradle chosen for backend.
- No full database is required; an in-memory data store (e.g., Java collections) is sufficient.
- Optional/ideal: Access to LLM models for programmatic (1) completions and (2) embeddings, e.g., via a local OpenAI-compatible HTTP server (LM Studio, etc.). The quality of LLM responses is not important for the interview; this is optional.
- You may use LLM-assisted IDEs during development. Parts of the interview may ask you to produce pseudo-code without LLM assistance.

## Provided Meal Plan Data (from prompt)
The prompt supplies a list of recipes in JSON form. The original snippet contains a couple of formatting issues (trailing comma, stray whitespace). Below is a corrected JSON payload that preserves the intent:

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
- Routing/navigation: A simple two-page flow is sufficient — list -> details. No need for create/edit/delete.
- Data source: Use in-memory storage initialized at startup from the provided JSON payload. No persistence across restarts is required.
- Units: Ingredient quantities include unit strings (e.g., "g"). We will treat these as display text and not parse units unless needed.
- IDs/slugs: Recipes can be identified by index, title-derived slug, or generated UUID; titles are assumed unique for simplicity.
- Error cases: Minimal handling is acceptable (e.g., 404 if a recipe is not found). No authentication/authorization required.
- Front end: Minimal HTML or a lightweight framework (e.g., server-side templates or a simple static SPA) is acceptable. No styling requirements.
- LLM usage: Entirely optional. If used, keep it behind a feature flag or simple configuration.
- Performance: The dataset is tiny; no special performance work is necessary.

## Suggested API Shape (minimal)
- GET `/api/recipes` -> returns an array of recipes with `title`, `yield`, and optionally an `id`/`slug`.
- GET `/api/recipes/{id}` -> returns the full recipe including `ingredients`.

Front-end pages can consume these endpoints or be rendered server-side.

## How to Run (this repository)
This repo uses Spring Boot with Gradle.

Prerequisites:
- Java 21 (or the version specified in `build.gradle` if different)
- Gradle wrapper (included): `gradlew`/`gradlew.bat`

Commands:
- Windows PowerShell/CMD: `gradlew.bat bootRun`
- macOS/Linux (if applicable): `./gradlew bootRun`

The app will start on `http://localhost:8080/` by default. Adjust the port in `src/main/resources/application.properties` if needed.

## How to Test (lightweight)
- Manual verification via browser or `curl`/Postman is sufficient per the prompt (tests not required).
- If you do add tests, run with: `gradlew.bat test`.

## Optional: LLM Integration Notes
If you choose to integrate LLM calls:
- Provide two client methods: `complete(prompt)` and `embed(texts[])` using an OpenAI-compatible base URL and API key.
- Make base URL and key configurable via environment variables or `application.properties`.
- Keep the feature optional; fall back to non-LLM behavior if not configured.

## Future Enhancements (out of scope for now)
- Persist recipes in a real database.
- Add create/edit/delete UI.
- Improve error handling, input validation, and accessibility.
- Add automated tests and CI.

## Metadata
- Local date/time for this attempt: 2025-11-05 12:08



## Frontend (Angular + Angular Material)
A minimal Angular 18 UI is included in the ui/ directory, styled with the latest Angular Material.

Prerequisites:
- Node.js 20+ and npm 10+

Run the UI in development mode:
1. Open a terminal in ui/
2. Install dependencies: npm install
3. Start the dev server with backend proxy: npm start
   - This serves the UI on http://localhost:4200
   - API requests to /recipe are proxied to the Spring Boot backend at http://localhost:8080

Notes:
- Ensure the Spring Boot backend is running (gradlew.bat bootRun) so the UI can fetch data.
- Material theme is set to indigo-pink via prebuilt theme; you can switch themes in ui/angular.json styles.
- The UI provides:
  - A list of recipes loaded from GET /recipe
  - A details view with ingredients
  - Generated instructions loaded from GET /recipe/{title}/instructions
