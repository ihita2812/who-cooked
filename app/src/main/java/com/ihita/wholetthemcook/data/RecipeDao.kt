package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Upsert
    suspend fun insertOrUpdate(recipe: Recipe)

    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long

    @Insert
    suspend fun insertAll(recipes: List<Recipe>): List<Long>

    @Query("SELECT * FROM Recipe ORDER BY id DESC")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Long): Recipe

    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getRecipeByIdFlow(id: Long): Flow<Recipe?>

    @Query("DELETE FROM Recipe WHERE id = :recipeId")
    suspend fun deleteById(recipeId: Long)

    @Update
    suspend fun updateRecipe(recipe: Recipe)
}
