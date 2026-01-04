package com.ihita.wholetthemcook.data

import java.util.Date
import com.ihita.wholetthemcook.firebase.FirestoreSync

object RecipeRepository {

    suspend fun saveRecipeWithIngredients(recipeId: Long?, title: String, process: List<String>, notes: String?, ingredients: List<ExportIngredient>) {

//        val id = if (recipeId == null) {
//            Database.recipeDao.insertRecipe(
//                Recipe(
//                    title = title,
//                    process = process,
//                    notes = notes,
//                    dateAdded = Date(),
//                    dateOpened = Date()
//                )
//            )
//        } else {
//            Database.recipeDao.updateRecipe(
//                Recipe(
//                    id = recipeId,
//                    title = title,
//                    process = process,
//                    notes = notes,
//                    dateAdded = Database.recipeDao.getRecipeById(recipeId).dateAdded,
//                    dateOpened = Date()
//                )
//            )
//            recipeId
//        }

        val recipe = if (recipeId == null) {
            val newId = Database.recipeDao.insertRecipe(
                Recipe(
                    title = title,
                    process = process,
                    notes = notes,
                    dateAdded = Date(),
                    dateOpened = Date()
                )
            )

            Database.recipeDao.getRecipeById(newId)
        } else {
            val updated = Recipe(
                id = recipeId,
                title = title,
                process = process,
                notes = notes,
                dateAdded = Database.recipeDao.getRecipeById(recipeId).dateAdded,
                dateOpened = Date()
            )

            Database.recipeDao.updateRecipe(updated)
            updated
        }
        FirestoreSync.uploadRecipe(recipe)

        Database.ingredientSetDao.deleteForRecipe(recipe.id)
        FirestoreSync.deleteIngredientSetsForRecipe(recipe.id)

        ingredients.forEach { ingredientIn ->
            if (ingredientIn.name.isBlank()) return@forEach

//            val ingredientId =
//                Database.ingredientDao.getByName(ingredientIn.name)
//                    ?.id ?: Database.ingredientDao.insertIngredient(Ingredient(title = ingredientIn.name))
            val ingredient = Database.ingredientDao.getByName(ingredientIn.name) ?: run {
                val newId = Database.ingredientDao.insertIngredient(Ingredient(title = ingredientIn.name))
                Ingredient(id = newId, title = ingredientIn.name)
            }
            FirestoreSync.uploadIngredient(ingredient)

//            Database.ingredientSetDao.insertIngredientSet(
//                IngredientSet(
//                    recipeId = id,
//                    ingredientId = ingredientId,
//                    quantity = ingredientIn.quantity?.toFloatOrNull(),
//                    unit = ingredientIn.unit,
//                    notes = ingredientIn.notes
//                )
//            )

            val set = IngredientSet(
                recipeId = recipe.id,
                ingredientId = ingredient.id,
                quantity = ingredientIn.quantity?.toFloatOrNull(),
                unit = ingredientIn.unit,
                notes = ingredientIn.notes
            )
            Database.ingredientSetDao.insertIngredientSet(set)
            FirestoreSync.uploadIngredientSet(set)

        }
    }
}
