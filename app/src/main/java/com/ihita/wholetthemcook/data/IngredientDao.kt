package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface IngredientDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Upsert
    suspend fun insertOrUpdate(ingredient: Ingredient)

    @Query("DELETE FROM Ingredient WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert
    suspend fun insertIngredient(ingredient: Ingredient): Long

    @Insert
    suspend fun insertAll(ingredients: List<Ingredient>): List<Long>

    @Query("SELECT DISTINCT title FROM Ingredient WHERE title LIKE :query || '%'")
    suspend fun searchIngredients(query: String): List<String>

//    @Query("SELECT units FROM Ingredient WHERE title = :ingredientName LIMIT 1")
//    suspend fun getUnitsForIngredient(ingredientName: String): String?

    @Query("SELECT * FROM Ingredient WHERE title = :name LIMIT 1")
    suspend fun getByName(name: String): Ingredient?
}
