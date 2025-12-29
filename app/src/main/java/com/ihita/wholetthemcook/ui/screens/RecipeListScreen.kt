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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.viewmodel.RecipeListViewModel

@Composable
fun RecipeListScreen(viewModel: RecipeListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val recipes by viewModel.recipes.collectAsState()

    LazyColumn {
        items(recipes) { recipe ->
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
