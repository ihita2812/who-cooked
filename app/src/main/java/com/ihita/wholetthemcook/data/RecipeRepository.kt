package com.ihita.wholetthemcook.data

import androidx.room.withTransaction
import com.ihita.wholetthemcook.ui.components.IngredientInput
import java.util.Date

object RecipeRepository {

    suspend fun saveRecipeWithIngredients(recipeId: Long?, title: String, process: String?, notes: String?, ingredients: List<IngredientInput>) {

        val id = if (recipeId == null) {
            Database.recipeDao.insertRecipe(
                Recipe(
                    title = title,
                    process = process,
                    notes = notes,
                    dateAdded = Date(),
                    dateOpened = Date()
                )
            )
        } else {
            Database.recipeDao.updateRecipe(
                Recipe(
                    id = recipeId,
                    title = title,
                    process = process,
                    notes = notes,
                    dateAdded = Database.recipeDao.getRecipeById(recipeId).dateAdded,
                    dateOpened = Date()
                )
            )
            recipeId
        }

        Database.ingredientSetDao.deleteForRecipe(id)

        ingredients.forEach { ingredientIn ->
            if (ingredientIn.name.isBlank()) return@forEach

            val ingredientId = Database.ingredientDao.getByName(ingredientIn.name)?.id ?: Database.ingredientDao.insertIngredient(Ingredient(title = ingredientIn.name))

            Database.ingredientSetDao.insertIngredientSet(
                IngredientSet(
                    recipeId = id,
                    ingredientId = ingredientId,
                    quantity = ingredientIn.quantity.toFloatOrNull(),
                    unit = ingredientIn.unit.ifBlank { null },
                    notes = ingredientIn.notes.ifBlank { null }
                )
            )
        }
    }
}
