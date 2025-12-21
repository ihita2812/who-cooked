package com.ihita.wholetthemcook.data

import android.content.Context
import androidx.room.Room

import com.ihita.wholetthemcook.data.RecipeDao
import com.ihita.wholetthemcook.data.IngredientDao
import com.ihita.wholetthemcook.data.IngredientSetDao


object Database {
    private var db: AppDatabase? = null

    private fun getDb(): AppDatabase =
        db ?: error("Database not initialized. Call Database.init(context) first.")

    fun init(context: Context) {
        db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "who-cooked-db").build()
    }

    val recipeDao: RecipeDao
        get() = getDb().recipeDao()
    val ingredientDao: IngredientDao
        get() = getDb().ingredientDao()
    val ingredientSetDao: IngredientSetDao
        get() = getDb().ingredientSetDao()
}