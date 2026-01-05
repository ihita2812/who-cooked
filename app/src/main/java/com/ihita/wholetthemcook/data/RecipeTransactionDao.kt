package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeTransactionDao {

    @Transaction
    suspend fun replaceIngredientsForRecipe(recipe: Recipe, ingredients: List<IngredientSet>) {
        deleteForRecipe(recipe.id)
        insertAll(ingredients)
    }

    @Query("DELETE FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun deleteForRecipe(recipeId: Long)

    @Insert
    suspend fun insertAll(sets: List<IngredientSet>)
}
