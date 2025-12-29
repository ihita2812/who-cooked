package com.ihita.wholetthemcook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.ihita.wholetthemcook.ui.screens.*

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
            RecipeListScreen(navController)
        }

        composable(
            route = "recipeInfo/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) {
            val recipeId = it.arguments?.getInt("recipeId")!!
            RecipeInfoScreen(recipeId)
        }

    }
}
