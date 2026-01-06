package com.ihita.wholetthemcook.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ihita.wholetthemcook.R
import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.navigation.Routes
import com.ihita.wholetthemcook.ui.components.SortOption
import com.ihita.wholetthemcook.ui.export.RecipePdfExporter
import com.ihita.wholetthemcook.viewmodel.RecipeListViewModel
import kotlinx.coroutines.launch

@Composable
fun RecipeListScreen(navController: NavController) {

    val listViewModel: RecipeListViewModel = viewModel()
    val recipes by listViewModel.recipes.collectAsState()
    val searchQuery by listViewModel.searchQuery.collectAsState()
    val selectedIds by listViewModel.selectedRecipeIds.collectAsState()
    val isSelectionMode by listViewModel.isSelectionMode.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        uri?.let {
            scope.launch {
                context.contentResolver.openOutputStream(it)?.use { out ->
                    val recipes = listViewModel.getSelectedExportRecipes()
                    RecipePdfExporter.export(recipes, out)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (isSelectionMode) {
                    SelectionTopBar(
                        selectedCount = selectedIds.size,
                        onDelete = { showDeleteDialog = true },
                        onEdit = {
                            val id = selectedIds.first()
                            navController.navigate("${Routes.ROUTE_EDIT_RECIPE}/$id")
                            listViewModel.clearSelection()
                        },
                        onExport = { exportLauncher.launch("recipes.pdf") },
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
                        onClick = { navController.navigate(Routes.ROUTE_ADD_RECIPE) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add recipe")
                    }
                }
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text("Delete recipes?") },
            text = { Text("Delete ${selectedIds.size} recipe(s)?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        listViewModel.deleteSelectedRecipes()
                    }
                ) {
                    Text(
                        "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
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
fun SelectionTopBar(selectedCount: Int, onDelete: () -> Unit, onEdit: () -> Unit, onExport: () -> Unit, onClearSelection: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$selectedCount selected",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (selectedCount == 1) {
                TextButton(onClick = onEdit) { Text("Edit") }
            }
            TextButton(onClick = onExport) { Text("Export") }
            TextButton(onClick = onDelete) { Text("Delete") }
            TextButton(onClick = onClearSelection) { Text("Clear") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(searchQuery: String, onSearchChange: (String) -> Unit, onSortSelected: (SortOption) -> Unit) {
    var showSortMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search recipes") },
            singleLine = true,
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Box {
            IconButton(
                onClick = { showSortMenu = true },
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Sort,
                    contentDescription = "Sort"
                )
            }

            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                DropdownMenuItem(text = { Text("Date added") }, onClick = {
                    onSortSelected(SortOption.DATE_ADDED)
                    showSortMenu = false
                })
                DropdownMenuItem(text = { Text("Date modified") }, onClick = {
                    onSortSelected(SortOption.DATE_MODIFIED)
                    showSortMenu = false
                })
                DropdownMenuItem(text = { Text("A → Z") }, onClick = {
                    onSortSelected(SortOption.TITLE_ASC)
                    showSortMenu = false
                })
                DropdownMenuItem(text = { Text("Z → A") }, onClick = {
                    onSortSelected(SortOption.TITLE_DESC)
                    showSortMenu = false
                })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeRow(recipe: Recipe, isSelected: Boolean, onClick: () -> Unit, onLongClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f)
                else
                    Color.Transparent
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                )
        )
    }
}
