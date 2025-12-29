package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IngredientDao {
    @Insert
    suspend fun insertIngredient(ingredient: Ingredient): Long

    @Query("SELECT DISTINCT title FROM Ingredient WHERE title LIKE :query || '%'")
    suspend fun searchIngredients(query: String): List<String>

    @Query("SELECT units FROM Ingredient WHERE title = :ingredientName LIMIT 1")
    suspend fun getUnitsForIngredient(ingredientName: String): String?
}
