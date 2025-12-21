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
import com.ihita.wholetthemcook.data.AppDatabase
import com.ihita.wholetthemcook.data.RecipeDao
import com.ihita.wholetthemcook.data.Recipe

class MainActivity : ComponentActivity() {
//    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "who-cooked-db").build()
//        val recipeDao = db.recipeDao()
//        val ingredientDao = db.ingredientDao()
//        val ingredientSetDao = db.ingredientSetDao()

        Database.init(applicationContext)

//        // -----------------DB TESTING-----------------
//        lifecycleScope.launch {
//            val id = Database.recipeDao.insertRecipe(
//                Recipe(
//                    title = "Test Recipe",
//                    process = "Mix everything",
//                    notes = "Suspend test"
//                )
//            )
//            println("id inserted: $id")
//
//            val recipes = Database.recipeDao.getAllRecipes()
//            println("RECIPES: $recipes")
//        }
//        // -----------------DB TESTING-----------------

        enableEdgeToEdge()
        setContent {
            WhoLetThemCookTheme() {
                WhoLetThemCookNavGraph()
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    WhoLetThemCookTheme {
//        Greeting("ihita")
//    }
//}