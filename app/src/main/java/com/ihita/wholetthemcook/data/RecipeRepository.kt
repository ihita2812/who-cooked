package com.ihita.wholetthemcook.data

import java.util.Date

import com.ihita.wholetthemcook.firebase.FirestoreSync
import com.ihita.wholetthemcook.firebase.model.FirestoreIngredientSet

object RecipeRepository {

    suspend fun saveRecipeWithIngredients(recipeId: Long?, title: String, process: List<String>, notes: String?, ingredients: List<ExportIngredient>) {

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
            val new = Database.recipeDao.getRecipeById(newId)
            FirestoreSync.uploadRecipe(new)
            new
        } else {
            val existing = Database.recipeDao.getRecipeById(recipeId)
            val updated = existing.copy(
                title = title,
                process = process,
                notes = notes,
                dateOpened = Date()
            )
            Database.recipeDao.updateRecipe(updated)
            updated
        }

        val ingredientSets = ingredients
            .filter { it.name.isNotBlank() }
            .map { ingredientIn ->

                val ingredient = Database.ingredientDao.getByName(ingredientIn.name) ?: run {
                    val newId = Database.ingredientDao.insertIngredient(Ingredient(title = ingredientIn.name))
                    val new = Ingredient(id = newId, title = ingredientIn.name)
                    FirestoreSync.uploadIngredient(new)
                    new
                }

                IngredientSet(
                    recipeId = recipe.id,
                    ingredientId = ingredient.id,
                    quantity = ingredientIn.quantity?.toFloatOrNull(),
                    unit = ingredientIn.unit,
                    notes = ingredientIn.notes
                )
            }

//        Database.getDb().runInTransaction {
//            Database.ingredientSetDao.deleteForRecipe(recipe.id)
//            Database.ingredientSetDao.insertAll(ingredientSets)
//        }

        Database.recipeTransactionDao.replaceIngredientsForRecipe(recipe.id, ingredientSets)

        FirestoreSync.deleteIngredientSetsForRecipe(recipe.id)
        ingredientSets.forEach { set ->
//            FirestoreSync.uploadIngredientSet(set)
            try {
                FirestoreSync.uploadIngredientSet(
                    FirestoreIngredientSet(
//                        id = set.id,
                        recipeId = set.recipeId,
                        ingredientId = set.ingredientId,
                        quantity = set.quantity,
                        unit = set.unit,
                        notes = set.notes
                    )
                )
            } catch (e: Exception) {
                println("Error while firestore pushing ingredientset.")
                println("${e}")
                println("-------------------------------------------")
                println("recipeid: ${set.recipeId}, ingredientid: ${set.ingredientId}")
                println("-------------------------------------------")
            }
        }
    }
}
