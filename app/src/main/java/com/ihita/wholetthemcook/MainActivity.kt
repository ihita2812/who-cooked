package com.ihita.wholetthemcook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.room.Room

import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.ui.theme.WhoLetThemCookTheme
import com.ihita.wholetthemcook.navigation.WhoLetThemCookNavGraph
import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.DummyDataSeeder
import com.ihita.wholetthemcook.data.Recipe

class MainActivity : ComponentActivity() {
//    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        // -----------------DB TESTING-----------------
//        // without lifecycle scope
//        // --------------------------------------------
//        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "who-cooked-db").build()
//        val recipeDao = db.recipeDao()
//        val ingredientDao = db.ingredientDao()
//        val ingredientSetDao = db.ingredientSetDao()
//        // -----------------DB TESTING-----------------

        Database.init(applicationContext)

//        // -----------------DB TESTING-----------------
//        // without actual db connection
//        // --------------------------------------------
//        lifecycleScope.launch {
//            val id = Database.recipeDao.insertRecipe(
//                Recipe(
//                    title = "idli",
//                    process = "hit up gay man",
//                    notes = "test1"
//                )
//            )
//            println("id inserted: $id")
//
//            val recipes = Database.recipeDao.getAllRecipes()
//            println("RECIPES: $recipes")
//        }
//        // -----------------DB TESTING-----------------

//        // -----------------DB TESTING-----------------
//        // actual db connection
//        // --------------------------------------------
//        lifecycleScope.launch {
//            Database.recipeDao.insertRecipe(
//                Recipe(
//                    title = "idli",
//                    process = "hit up gay man",
//                    notes = "test4",
//                    dateAdded = "301225",
//                    dateOpened = "301225"
//                )
//            )
//        }
//        // -----------------DB TESTING-----------------

        lifecycleScope.launch {
            val dao = Database.recipeDao
//            DummyDataSeeder.seed(Database)
        }

        enableEdgeToEdge()
        setContent {
            WhoLetThemCookTheme() {
                WhoLetThemCookNavGraph()
            }
        }
    }
}
