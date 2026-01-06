package com.ihita.wholetthemcook.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.navigation.Routes
import com.ihita.wholetthemcook.ui.components.ActionIcon
import com.ihita.wholetthemcook.ui.components.PaperScreen
import com.ihita.wholetthemcook.ui.components.SortOption
import com.ihita.wholetthemcook.ui.export.RecipePdfExporter
import com.ihita.wholetthemcook.ui.theme.*
import com.ihita.wholetthemcook.viewmodel.RecipeListViewModel

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
    val exportLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
            uri?.let {
                scope.launch {
                    context.contentResolver.openOutputStream(it)?.use { out ->
                        RecipePdfExporter.export(
                            listViewModel.getSelectedExportRecipes(),
                            out
                        )
                    }
                }
            }
        }

    PaperScreen {

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
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
                        containerColor = ButtonPrimary,
                        contentColor = DarkPurple,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add recipe"
                        )
                    }
                }
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
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
            containerColor = LightLilac,
            title = {
                Text(
                    text = "Delete recipes?",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkPurple
                )
            },
            text = {
                Text(
                    text = "Delete ${selectedIds.size} recipe(s)?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBody
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    listViewModel.deleteSelectedRecipes()
                }) {
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkPurple
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelSmall,
                        color = LighterPurple
                    )
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
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "$selectedCount selected",
            style = MaterialTheme.typography.bodyMedium,
            color = TextBody
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (selectedCount == 1) ActionIcon(icon = Icons.Default.Edit, contentDescription = "Edit", onClick = onEdit)
            ActionIcon(icon = Icons.Default.Share, contentDescription = "Export", onClick = onExport)
            ActionIcon(icon = Icons.Default.Delete, contentDescription = "Delete", onClick = onDelete)
            ActionIcon(icon = Icons.Default.Close, contentDescription = "Clear selection", onClick = onClearSelection)
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
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text(
                    text = "Search recipes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextBody),
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LightLilac.copy(alpha = 0.85f),
                unfocusedContainerColor = LightLilac.copy(alpha = 0.7f),
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                cursorColor = LighterPurple
            )
        )

        Box {
            ActionIcon(
                icon = Icons.Default.Sort,
                contentDescription = "Sort",
                onClick = { showSortMenu = true }
            )

            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false },
                modifier = Modifier.background(LightLilac)
            ) {
                SortOptionItem("Date added") {
                    onSortSelected(SortOption.DATE_ADDED)
                    showSortMenu = false
                }
                SortOptionItem("Date modified") {
                    onSortSelected(SortOption.DATE_MODIFIED)
                    showSortMenu = false
                }
                SortOptionItem("A → Z") {
                    onSortSelected(SortOption.TITLE_ASC)
                    showSortMenu = false
                }
                SortOptionItem("Z → A") {
                    onSortSelected(SortOption.TITLE_DESC)
                    showSortMenu = false
                }
            }
        }
    }
}

@Composable
private fun SortOptionItem(text: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = TextBody
            )
        },
        onClick = onClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeRow(recipe: Recipe, isSelected: Boolean, onClick: () -> Unit, onLongClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .background(
                if (isSelected)
                    ButtonSecondary.copy(alpha = 0.25f)
                else
                    androidx.compose.ui.graphics.Color.Transparent,
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Text(
            text = recipe.title,
            style = MaterialTheme.typography.titleMedium,
            color = DarkPurple
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LighterPurple.copy(alpha = 0.25f))
        )
    }
}
