package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface IngredientSetDao {
    @Insert
    suspend fun insertIngredientSet(set: IngredientSet)

    @Query("SELECT * FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun getIngredientSetsForRecipe(recipeId: Int): List<IngredientSet>

    @Transaction
    @Query("SELECT * FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Long): List<IngredientRecipe>

    @Insert
    suspend fun insertAll(sets: List<IngredientSet>)

    @Query("DELETE FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun deleteForRecipe(recipeId: Long)
}
