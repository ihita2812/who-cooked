package com.ihita.wholetthemcook.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModel
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModelFactory


@Composable
fun RecipeInfoScreen(recipeId: Int) {

    val viewModel: RecipeInfoViewModel = viewModel(
        factory = RecipeInfoViewModelFactory(recipeId)
    )

    val recipe by viewModel.recipe.collectAsState()

    recipe?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it.process.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it.notes.toString())
        }
    }
}
