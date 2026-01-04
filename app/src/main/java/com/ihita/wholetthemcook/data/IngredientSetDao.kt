package com.ihita.wholetthemcook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientSetDao {

    @Insert
    suspend fun insertIngredientSet(set: IngredientSet)

    @Insert
    suspend fun insertAll(sets: List<IngredientSet>)

    @Query("DELETE FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun deleteForRecipe(recipeId: Long)

    @Query("SELECT * FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun getIngredientSetsForRecipe(recipeId: Long): List<IngredientSet>

    @Query("SELECT * FROM IngredientSet WHERE recipeId = :recipeId")
    fun getIngredientSetsForRecipeFlow(recipeId: Long): Flow<List<IngredientSet>>

    @Transaction
    @Query("SELECT * FROM IngredientSet WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Long): List<IngredientRecipe>

    @Transaction
    @Query("SELECT * FROM IngredientSet WHERE recipeId = :recipeId")
    fun getIngredientsForRecipeFlow(recipeId: Long): Flow<List<IngredientRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(set: IngredientSet)

    @Query("DELETE FROM IngredientSet WHERE recipeId = :recipeId AND ingredientId = :ingredientId")
    suspend fun deleteByRecipeAndIngredient(recipeId: Long, ingredientId: Long)
}
