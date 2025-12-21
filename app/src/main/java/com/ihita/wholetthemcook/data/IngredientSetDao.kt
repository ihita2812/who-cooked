package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IngredientSetDao {
    @Insert
    suspend fun insertIngredientSet(set: IngredientSet)

    @Query("SELECT * FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Int): List<IngredientSet>
}
