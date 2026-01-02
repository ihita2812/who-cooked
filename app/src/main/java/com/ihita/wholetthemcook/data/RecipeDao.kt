package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.stream.DoubleStream.iterate

@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long

    @Query("SELECT * FROM Recipe ORDER BY id DESC")
    fun getAllRecipes(): kotlinx.coroutines.flow.Flow<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Long): Recipe

    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getRecipeByIdFlow(id: Long): Flow<Recipe?>

    @Query("DELETE FROM Recipe WHERE id = :recipeId")
    suspend fun deleteById(recipeId: Long)

    @Update
    suspend fun updateRecipe(recipe: Recipe)
}
