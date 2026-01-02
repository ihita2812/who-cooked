package com.ihita.wholetthemcook.ui.screens

import android.text.Selection
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.ihita.wholetthemcook.navigation.Routes
import com.ihita.wholetthemcook.viewmodel.RecipeListViewModel
import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.ui.components.SortOption

@Composable
fun RecipeListScreen(navController: NavController) {

    val listViewModel: RecipeListViewModel = viewModel()
    val recipes by listViewModel.recipes.collectAsState()
    val searchQuery by listViewModel.searchQuery.collectAsState()
    val selectedIds by listViewModel.selectedRecipeIds.collectAsState()
    val isSelectionMode by listViewModel.isSelectionMode.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (isSelectionMode) {
                SelectionTopBar(
                    selectedCount = selectedIds.size,
//                    onDelete = listViewModel::deleteSelectedRecipes,
                    onDelete = { showDeleteDialog = true },
                    onEdit = {
                        val id = selectedIds.first()
                        navController.navigate("${Routes.ROUTE_EDIT_RECIPE}/$id")
                        listViewModel.clearSelection()
                    },
                    // onExport = /* TODO */,
                    onClearSelection = listViewModel::clearSelection
                )
            } else {
                DefaultTopBar(
                    searchQuery = searchQuery,
                    onSearchChange = listViewModel::updateSearchQuery,
                    onSortSelected = listViewModel::updateSortOption
                )
            }
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.ROUTE_ADD_RECIPE) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add recipe")
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete recipes?") },
            text = { Text("Are you sure you want to delete ${selectedIds.size} recipe(s)?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        listViewModel.deleteSelectedRecipes()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
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

@Composable
fun SelectionTopBar(selectedCount: Int, onDelete: () -> Unit, onEdit: () -> Unit, onClearSelection: () -> Unit) {

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

        //    Button(onClick = onExport) {
        //        Text("Export")
        //    }

            Button(onClick = onDelete) {
                Text("Delete")
            }

            Button(onClick = onClearSelection) {
                Text("Clear")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(searchQuery: String, onSearchChange: (String) -> Unit, onSortSelected: (SortOption) -> Unit) {
    var showSortMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search recipes") },
                singleLine = true,
                modifier = Modifier,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        },
        actions = {
            IconButton(onClick = { showSortMenu = true }) {
                Icon(imageVector = Icons.Filled.Sort, contentDescription = "Sort")
            }

            DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                DropdownMenuItem(
                    text = { Text("Date added") },
                    onClick = {
                        onSortSelected(SortOption.DATE_ADDED)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Date modified") },
                    onClick = {
                        onSortSelected(SortOption.DATE_MODIFIED)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("A → Z") },
                    onClick = {
                        onSortSelected(SortOption.TITLE_ASC)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Z → A") },
                    onClick = {
                        onSortSelected(SortOption.TITLE_DESC)
                        showSortMenu = false
                    }
                )
            }
        }
    )
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
