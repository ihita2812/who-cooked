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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModel
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModelFactory
import com.ihita.wholetthemcook.navigation.Routes

@Composable
fun RecipeInfoScreen(navController: NavController, recipeId: Long) {

    val infoViewModel: RecipeInfoViewModel = viewModel(factory = RecipeInfoViewModelFactory(recipeId))
    val recipe by infoViewModel.recipe.collectAsState()
    val isDeleted by infoViewModel.isDeleted.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

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

            Button(onClick = { showDeleteDialog = true }) {
                Text("Delete")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },

            title = { Text("Delete recipe?") },
            text = { Text("Are you sure you want to delete \"${recipe?.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        infoViewModel.deleteRecipe()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

}
