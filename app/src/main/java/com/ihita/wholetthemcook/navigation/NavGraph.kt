package com.ihita.wholetthemcook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ihita.wholetthemcook.ui.screens.HomeScreen
import com.ihita.wholetthemcook.ui.screens.RecipeListScreen

@Composable
fun WhoLetThemCookNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToRecipeList = {
                    navController.navigate("recipe_list")
                }
            )
        }

        composable("recipe_list") {
            RecipeListScreen()
        }
    }
}
