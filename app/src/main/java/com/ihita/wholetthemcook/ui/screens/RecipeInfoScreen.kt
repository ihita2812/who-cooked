package com.ihita.wholetthemcook.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ihita.wholetthemcook.navigation.Routes
import com.ihita.wholetthemcook.ui.components.PaperScreen
import com.ihita.wholetthemcook.ui.export.RecipePdfExporter
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModel
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModelFactory

@Composable
fun RecipeInfoScreen(navController: NavController, recipeId: Long) {

    val infoViewModel: RecipeInfoViewModel =
        viewModel(factory = RecipeInfoViewModelFactory(recipeId))

    val recipe by infoViewModel.recipe.collectAsState()
    val ingredients by infoViewModel.ingredients.collectAsState()
    val isDeleted by infoViewModel.isDeleted.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val exportLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("application/pdf")
        ) { uri ->
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { out ->
                    infoViewModel.getExportRecipe()?.let { recipe ->
                        RecipePdfExporter.export(listOf(recipe), out)
                    }
                }
            }
        }

    LaunchedEffect(isDeleted) {
        if (isDeleted) navController.popBackStack()
    }

    val bodyTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.88f)
    val accentUnderline = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)

    PaperScreen {

        if (recipe == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading recipe…",
                    color = bodyTextColor
                )
            }
            return@PaperScreen
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp), // respects paper border
            contentPadding = PaddingValues(vertical = 28.dp)
        ) {

            /* ---------- TITLE ---------- */

            item {
                Text(
                    text = recipe!!.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = bodyTextColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(3.dp)
                        .background(
                            color = accentUnderline,
                            shape = MaterialTheme.shapes.small
                        )
                )

                Spacer(modifier = Modifier.height(36.dp))
            }

            /* ---------- INGREDIENTS ---------- */

            item {
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    color = bodyTextColor,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }

            items(ingredients) { item ->
                Text(
                    text = buildString {
                        append("• ")
                        append(item.ingredient.title)
                        item.ingredientSet.quantity?.let {
                            append(" – ")
                            append(it)
                            item.ingredientSet.unit?.let { unit ->
                                append(" $unit")
                            }
                        }
                        item.ingredientSet.notes
                            ?.takeIf { it.isNotBlank() }
                            ?.let { append(" ($it)") }
                    },
                    color = bodyTextColor.copy(alpha = 0.95f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            /* ---------- PROCESS ---------- */

            item {
                Text(
                    text = "Process",
                    style = MaterialTheme.typography.titleMedium,
                    color = bodyTextColor,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }

            itemsIndexed(recipe!!.process) { index, step ->
                Text(
                    text = "${index + 1}. $step",
                    color = bodyTextColor,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            /* ---------- NOTES ---------- */

            if (!recipe!!.notes.isNullOrBlank()) {
                item { Spacer(modifier = Modifier.height(40.dp)) }

                item {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        color = bodyTextColor,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                }

                item {
                    Text(
                        text = recipe!!.notes!!,
                        color = bodyTextColor.copy(alpha = 0.95f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }

            /* ---------- ACTIONS ---------- */

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("${Routes.ROUTE_EDIT_RECIPE}/$recipeId")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit")
                    }

                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete")
                    }

                    Button(
                        onClick = {
                            exportLauncher.launch("${recipe!!.title}.pdf")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Export")
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete recipe?") },
            text = { Text("Are you sure you want to delete \"${recipe!!.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        infoViewModel.deleteRecipe()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
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
