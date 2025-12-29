package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ihita.wholetthemcook.viewmodel.RecipeListViewModel

@Composable
fun RecipeListScreen(
    navController: NavHostController,
    viewModel: RecipeListViewModel = viewModel()
) {
    val recipes by viewModel.recipes.collectAsState()

//    Text("These are all the recipes!", modifier=Modifier.padding(16.dp))

    LazyColumn {
        items(recipes) { recipe ->
            Text(
                text = recipe.title,
                modifier = Modifier
                    .clickable {
                        navController.navigate("recipeInfo/${recipe.id}")
                    }
                    .padding(16.dp)
            )
        }
    }
}
