package com.ihita.wholetthemcook.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
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
import com.ihita.wholetthemcook.ui.export.QuantityFormatter
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModel
import com.ihita.wholetthemcook.viewmodel.RecipeInfoViewModelFactory
import com.ihita.wholetthemcook.ui.theme.*

@Composable
fun RecipeInfoScreen(navController: NavController, recipeId: Long) {

    val infoViewModel: RecipeInfoViewModel = viewModel(factory = RecipeInfoViewModelFactory(recipeId))

    val recipe by infoViewModel.recipe.collectAsState()
    val ingredients by infoViewModel.ingredients.collectAsState()
    val isDeleted by infoViewModel.isDeleted.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { out ->
                infoViewModel.getExportRecipe()?.let { recipe ->
                    RecipePdfExporter.export(context, listOf(recipe), out)
                }
            }
        }
    }

    LaunchedEffect(isDeleted) {
        if (isDeleted) navController.popBackStack()
    }

    PaperScreen {

        if (recipe == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBody
                )
            }
            return@PaperScreen
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = recipe!!.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkPurple
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .width(72.dp)
                                .height(3.dp)
                                .background(
                                    LighterPurple.copy(alpha = 0.45f),
                                    shape = MaterialTheme.shapes.small
                                )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        IconButton(
                            onClick = {
                                navController.navigate(
                                    "${Routes.ROUTE_EDIT_RECIPE}/$recipeId"
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit recipe",
                                tint = TextBody
                            )
                        }

                        IconButton(
                            onClick = {
                                exportLauncher.launch("${recipe!!.title}.pdf")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Export recipe",
                                tint = TextBody
                            )
                        }

                        IconButton(
                            onClick = { showDeleteDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete recipe",
                                tint = TextBody
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "INGREDIENTS",
                    style = MaterialTheme.typography.labelLarge,
                    color = LighterPurple
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ingredients.forEach { item ->
                        Text(
                            text = buildString {
                                append("• ")
                                append(item.ingredient.title)
                                item.ingredientSet.quantity?.let {
                                    append(" – ${QuantityFormatter.format(it)}")
                                    item.ingredientSet.unit?.let { unit ->
                                        append(" $unit")
                                    }
                                }
                                item.ingredientSet.notes
                                    ?.takeIf { it.isNotBlank() }
                                    ?.let { append(" ($it)") }
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextBody
                        )
                    }
                }
            }

            item {
                Text(
                    text = "PROCESS",
                    style = MaterialTheme.typography.labelLarge,
                    color = LighterPurple
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    recipe!!.process.forEachIndexed { index, step ->
                        Text(
                            text = "${index + 1}. $step",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextBody
                        )
                    }
                }
            }

            if (!recipe!!.notes.isNullOrBlank()) {
                item {
                    Text(
                        text = "NOTES",
                        style = MaterialTheme.typography.labelLarge,
                        color = LighterPurple
                    )
                }

                item {
                    Text(
                        text = recipe!!.notes!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBody
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete recipe?",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkPurple
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${recipe!!.title}\"?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextBody
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    infoViewModel.deleteRecipe()
                }) {
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextBody
                    )
                }
            }
        )
    }
}
