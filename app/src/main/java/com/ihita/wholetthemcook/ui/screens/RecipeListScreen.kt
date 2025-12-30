package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.ihita.wholetthemcook.navigation.Routes
import com.ihita.wholetthemcook.viewmodel.RecipeListViewModel
import com.ihita.wholetthemcook.data.Recipe

@Composable
fun RecipeListScreen(navController: NavController) {

    val listViewModel: RecipeListViewModel = viewModel()
    val recipes by listViewModel.recipes.collectAsState()
    val selectedIds by listViewModel.selectedRecipeIds.collectAsState()
    val isSelectionMode by listViewModel.isSelectionMode.collectAsState()

    Column {

        if (isSelectionMode) {
            RecipeListTopBar(
                isSelectionMode = true,
                selectedCount = selectedIds.size,
                onDelete = listViewModel::deleteSelectedRecipes,
                onEdit = {
                    val id = selectedIds.first()
                    navController.navigate("${Routes.ROUTE_EDIT_RECIPE}/$id")
                    listViewModel.clearSelection()
                },
//            onExport = /* TODO */,
                onClearSelection = listViewModel::clearSelection
            )
        }

        LazyColumn {
            items(recipes) { recipe ->
                RecipeRow(
                    recipe = recipe,
                    isSelected = selectedIds.contains(recipe.id),
                    onClick = {
                        if (isSelectionMode) {
                            listViewModel.toggleSelection(recipe.id)
                        } else {
                            navController.navigate("${Routes.RECIPE_INFO}/${recipe.id}")
                        }
                    },
                    onLongClick = {
                        listViewModel.toggleSelection(recipe.id)
                    }

                )
            }
        }

    }

}

@Composable
fun RecipeListTopBar(isSelectionMode: Boolean, selectedCount: Int, onDelete: () -> Unit, onEdit: () -> Unit, onClearSelection: () -> Unit) {
    if (!isSelectionMode) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$selectedCount selected")

        Row {
            if (selectedCount == 1) {
                Button(onClick = onEdit) {
                    Text("Edit")
                }
            }

//            Button(onClick = onExport) {
//                Text("Export")
//            }

            Button(onClick = onDelete) {
                Text("Delete")
            }

            Button(onClick = onClearSelection) {
                Text("Clear")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeRow(recipe: Recipe, isSelected: Boolean, onClick: () -> Unit, onLongClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.background
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(16.dp)
    ) {
        Text(text = recipe.title)
    }
}
