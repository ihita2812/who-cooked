package com.ihita.wholetthemcook.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

object DummyDataSeeder {

    suspend fun seed(database: Database) = withContext(Dispatchers.IO) {
        val ingredientIds = seedIngredients(database)
        val recipeIds = seedRecipes(database)
        seedIngredientSets(database, recipeIds, ingredientIds)
    }

    private suspend fun seedIngredients(database: Database): Map<String, Long> {
        val ingredients = listOf(
            Ingredient(title = "Salt"),
            Ingredient(title = "Sugar"),
            Ingredient(title = "Olive Oil"),
            Ingredient(title = "Butter"),
            Ingredient(title = "Garlic"),
            Ingredient(title = "Onion"),
            Ingredient(title = "Tomato"),
            Ingredient(title = "Milk"),
            Ingredient(title = "Flour"),
            Ingredient(title = "Eggs")
        )

        val ids = database.ingredientDao.insertAll(ingredients)
        return ingredients.mapIndexed { index, ingredient -> ingredient.title to ids[index] }.toMap()
    }

    private suspend fun seedRecipes(database: Database): List<Long> {
        val calendar = Calendar.getInstance()

        fun daysAgo(days: Int): Date {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -days)
            return calendar.time
        }

        val recipes = listOf(
            Recipe(
                title = "Classic Omelette",
                dateAdded = daysAgo(10),
                dateOpened = daysAgo(1),
                process = listOf("Beat eggs", "cook with butter", "fold gently"),
                notes = "Best with fresh eggs."
            ),
            Recipe(
                title = "Garlic Bread",
                dateAdded = daysAgo(20),
                dateOpened = daysAgo(5),
                process = listOf("Mix butter and garlic", "spread on bread", "bake"),
                notes = null
            ),
            Recipe(
                title = "Tomato Pasta",
                dateAdded = daysAgo(5),
                dateOpened = daysAgo(0),
                process = listOf("Cook pasta", "prepare tomato sauce", "combine"),
                notes = "Add basil if available."
            ),
            Recipe(
                title = "Pancakes",
                dateAdded = daysAgo(30),
                dateOpened = daysAgo(12),
                process = listOf("Mix dry and wet ingredients", "cook on pan"),
                notes = "Serve with honey or syrup."
            ),
            Recipe(
                title = "Simple Salad",
                dateAdded = daysAgo(2),
                dateOpened = daysAgo(2),
                process = emptyList(),
                notes = "Very flexible recipe."
            )
        )

        return database.recipeDao.insertAll(recipes)
    }

    private suspend fun seedIngredientSets(
        database: Database,
        recipeIds: List<Long>,
        ingredientIds: Map<String, Long>
    ) {
        val sets = listOf(
            // Omelette
            IngredientSet(
                recipeId = recipeIds[0],
                ingredientId = ingredientIds.getValue("Eggs"),
                quantity = 2f,
                unit = "pcs",
                notes = null
            ),
            IngredientSet(
                recipeId = recipeIds[0],
                ingredientId = ingredientIds.getValue("Butter"),
                quantity = 1f,
                unit = "tbsp",
                notes = "Unsalted preferred"
            ),
            IngredientSet(
                recipeId = recipeIds[0],
                ingredientId = ingredientIds.getValue("Salt"),
                quantity = null,
                unit = null,
                notes = "To taste"
            ),

            // Garlic Bread
            IngredientSet(
                recipeId = recipeIds[1],
                ingredientId = ingredientIds.getValue("Garlic"),
                quantity = 3f,
                unit = "cloves",
                notes = "Finely chopped"
            ),
            IngredientSet(
                recipeId = recipeIds[1],
                ingredientId = ingredientIds.getValue("Butter"),
                quantity = 2f,
                unit = "tbsp",
                notes = null
            ),

            // Tomato Pasta
            IngredientSet(
                recipeId = recipeIds[2],
                ingredientId = ingredientIds.getValue("Tomato"),
                quantity = 4f,
                unit = "pcs",
                notes = "Ripe"
            ),
            IngredientSet(
                recipeId = recipeIds[2],
                ingredientId = ingredientIds.getValue("Olive Oil"),
                quantity = 1f,
                unit = "tbsp",
                notes = null
            ),
            IngredientSet(
                recipeId = recipeIds[2],
                ingredientId = ingredientIds.getValue("Salt"),
                quantity = null,
                unit = null,
                notes = "To taste"
            ),

            // Pancakes
            IngredientSet(
                recipeId = recipeIds[3],
                ingredientId = ingredientIds.getValue("Flour"),
                quantity = 1.5f,
                unit = "cups",
                notes = null
            ),
            IngredientSet(
                recipeId = recipeIds[3],
                ingredientId = ingredientIds.getValue("Milk"),
                quantity = 1f,
                unit = "cup",
                notes = null
            ),
            IngredientSet(
                recipeId = recipeIds[3],
                ingredientId = ingredientIds.getValue("Eggs"),
                quantity = 1f,
                unit = "pc",
                notes = null
            ),

            // Simple Salad
            IngredientSet(
                recipeId = recipeIds[4],
                ingredientId = ingredientIds.getValue("Tomato"),
                quantity = 2f,
                unit = "pcs",
                notes = null
            ),
            IngredientSet(
                recipeId = recipeIds[4],
                ingredientId = ingredientIds.getValue("Olive Oil"),
                quantity = null,
                unit = null,
                notes = "Drizzle"
            )
        )

        database.ingredientSetDao.insertAll(sets)
    }
}
