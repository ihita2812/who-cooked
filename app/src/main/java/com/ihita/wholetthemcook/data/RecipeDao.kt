package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long  // returns the inserted recipe ID

    @Query("SELECT * FROM Recipe ORDER BY id DESC")
    fun getAllRecipes(): kotlinx.coroutines.flow.Flow<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Long): Recipe

    @Query("DELETE FROM Recipe WHERE id = :recipeId")
    suspend fun deleteById(recipeId: Long)
}
