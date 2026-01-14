package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeTransactionDao {

    @Transaction
    suspend fun replaceIngredientsForRecipe(recipeId: Long, ingredients: List<IngredientSet>) {
        deleteForRecipe(recipeId)
        upsertAll(ingredients)
    }

    @Query("DELETE FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun deleteForRecipe(recipeId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(sets: List<IngredientSet>)
}
