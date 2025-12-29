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
import androidx.navigation.NavController

import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModel
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModelFactory
import com.ihita.wholetthemcook.navigation.Routes

@Composable
fun RecipeInfoScreen(recipeId: Int, onBackClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Title
        Text(text = "Recipe Title (ID: $recipeId)")

        Spacer(modifier = Modifier.height(12.dp))

        // Ingredients
        Text(text = "Ingredients")
        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(3) { index ->
                Text(text = "• Ingredient $index – 2 cups")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Process
        Text(text = "Process")
        Text(text = "Step-by-step cooking process goes here.")

        Spacer(modifier = Modifier.height(12.dp))

        // Notes
        Text(text = "Notes")
        Text(text = "Optional notes go here.")

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBackClick) {
                Text("Back")
            }

            Button(onClick = {navController.navigate(route = "${Routes.ROUTE_EDIT_RECIPE}/${recipeId}")}) {
                Text("Edit")
            }

            Button(onClick = onDeleteClick) {
                Text("Delete")
            }
        }
    }
}
