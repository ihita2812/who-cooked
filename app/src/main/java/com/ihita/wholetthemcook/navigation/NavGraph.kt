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
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.RECIPE_LIST) {
            RecipeListScreen(navController)
        }

        composable(route = "${Routes.RECIPE_INFO}/{recipeId}") { backStackEntry ->

            val recipeId = backStackEntry.arguments
                ?.getString("recipeId")
                ?.toInt() ?: return@composable

            RecipeInfoScreen(
                recipeId = recipeId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { /* TODO */ },
                onDeleteClick = { /* TODO */ }
            )
        }
    }
}
