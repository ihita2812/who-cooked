package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long  // returns the inserted recipe ID

    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipes(): List<Recipe>
}
