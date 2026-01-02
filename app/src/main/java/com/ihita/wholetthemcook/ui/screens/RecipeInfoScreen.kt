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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModel
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModelFactory
import com.ihita.wholetthemcook.navigation.Routes

// navController.popBackStack
@Composable
fun RecipeInfoScreen(navController: NavController, recipeId: Long, onDeleteClick: () -> Unit) {

    val infoViewModel: RecipeInfoViewModel = viewModel(factory = RecipeInfoViewModelFactory(recipeId))
    val recipe by infoViewModel.recipe.collectAsState()
    val isDeleted by infoViewModel.isDeleted.collectAsState()

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            navController.popBackStack()
        }
    }

    if (recipe == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text = "Loading recipe...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Title
        Text(text = recipe!!.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        // Ingredients
        Text(text = "Ingredients", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        /* TODO */
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(3) { index ->
                Text(text = "• Ingredient $index – 2 cups")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Process
        Text(text = "Process", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = recipe!!.process ?: "")
        Spacer(modifier = Modifier.height(12.dp))

        // Notes
        if (!recipe!!.notes.isNullOrBlank()) {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = recipe!!.notes!!)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {navController.navigate(route = "${Routes.ROUTE_EDIT_RECIPE}/${recipeId}")}) {
                Text("Edit")
            }

            Button(onClick = { infoViewModel.deleteRecipe() }) {
                Text("Delete")
            }
        }
    }
}
