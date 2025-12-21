package com.ihita.wholetthemcook.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Recipe::class, Ingredient::class, IngredientSet::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun ingredientSetDao(): IngredientSetDao
}
