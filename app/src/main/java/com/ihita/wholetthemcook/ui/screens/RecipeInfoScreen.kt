package com.ihita.wholetthemcook.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.ihita.wholetthemcook.navigation.Routes
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
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
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

    if (recipe == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Loading recipe…",
                color = Color(0xFF4A453F)
            )
        }
        return
    }

    val bodyTextColor = Color(0xFF3E3A36)
    val accentUnderline = Color(0xFFD6B7E2)

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4EDE8).copy(alpha = 0.35f))
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {

            // Title
            item {
                Text(
                    text = recipe!!.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = bodyTextColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(3.dp)
                        .background(
                            color = accentUnderline.copy(alpha = 0.6f),
                            shape = MaterialTheme.shapes.small
                        )
                )

                Spacer(modifier = Modifier.height(28.dp))
            }

            // Ingredients
            item {
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    color = bodyTextColor,
                    modifier = Modifier.padding(bottom = 12.dp)
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
                    color = bodyTextColor.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Process
            item {
                Text(
                    text = "Process",
                    style = MaterialTheme.typography.titleMedium,
                    color = bodyTextColor,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            itemsIndexed(recipe!!.process) { index, step ->
                Text(
                    text = "${index + 1}. $step",
                    color = bodyTextColor.copy(alpha = 0.95f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (!recipe!!.notes.isNullOrBlank()) {
                item { Spacer(modifier = Modifier.height(32.dp)) }

                item {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        color = bodyTextColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                item {
                    Text(
                        text = recipe!!.notes!!,
                        color = bodyTextColor.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            // Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        navController.navigate("${Routes.ROUTE_EDIT_RECIPE}/$recipeId")
                    }) {
                        Text("Edit")
                    }

                    Button(onClick = { showDeleteDialog = true }) {
                        Text("Delete")
                    }

                    Button(onClick = {
                        exportLauncher.launch("${recipe!!.title}.pdf")
                    }) {
                        Text("Export PDF")
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
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
