package com.ihita.wholetthemcook.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Recipe::class, Ingredient::class, IngredientSet::class],
    version = 6,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun ingredientSetDao(): IngredientSetDao
    abstract fun recipeTransactionDao(): RecipeTransactionDao
}