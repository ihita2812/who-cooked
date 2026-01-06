package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.ExportIngredient
import com.ihita.wholetthemcook.data.RecipeRepository
import com.ihita.wholetthemcook.ui.components.IngredientRow
import com.ihita.wholetthemcook.ui.components.PaperScreen

@Composable
fun AddEditRecipeScreen(
    navController: NavController,
    recipeId: Long? = null
) {
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf<ExportIngredient>() }
    val processSteps = remember { mutableStateListOf("") }
    var notes by remember { mutableStateOf("") }

    val inkColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)

    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            val recipe = Database.recipeDao.getRecipeById(recipeId)
            title = recipe.title
            processSteps.clear()
            processSteps.addAll(recipe.process.ifEmpty { listOf("") })
            notes = recipe.notes ?: ""

            val ingredientRecipes =
                Database.ingredientSetDao.getIngredientsForRecipe(recipeId)

            ingredients.clear()
            ingredients.addAll(
                ingredientRecipes.map {
                    ExportIngredient(
                        name = it.ingredient.title,
                        quantity = it.ingredientSet.quantity?.toString() ?: "",
                        unit = it.ingredientSet.unit ?: "",
                        notes = it.ingredientSet.notes ?: ""
                    )
                }
            )

            if (ingredients.isEmpty()) {
                ingredients.add(ExportIngredient())
            }
        } else {
            ingredients.add(ExportIngredient())
        }
    }

    PaperScreen {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            /* ---------- SCREEN TITLE ---------- */

            item {
                Text(
                    text = if (recipeId == null) "New Recipe" else "Edit Recipe",
                    style = MaterialTheme.typography.headlineMedium,
                    color = inkColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .height(3.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                            shape = MaterialTheme.shapes.small
                        )
                )
            }

            /* ---------- RECIPE TITLE ---------- */

            item {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Recipe title") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.large
                )
            }

            /* ---------- INGREDIENTS ---------- */

            item {
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    color = inkColor
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ingredients.forEach { ingredient ->
                        IngredientRow(
                            ingredient = ingredient,
                            onChange = { updated ->
                                val index =
                                    ingredients.indexOfFirst { it.id == updated.id }
                                if (index != -1) ingredients[index] = updated
                            },
                            onDelete = {
                                if (ingredients.size > 1) {
                                    ingredients.remove(ingredient)
                                }
                            }
                        )
                    }
                }
            }

            item {
                TextButton(onClick = { ingredients.add(ExportIngredient()) }) {
                    Text("Add ingredient")
                }
            }

            /* ---------- PROCESS ---------- */

            item {
                Text(
                    text = "Process",
                    style = MaterialTheme.typography.titleMedium,
                    color = inkColor
                )
            }

            itemsIndexed(processSteps) { index, step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}.",
                        color = inkColor,
                        modifier = Modifier.width(24.dp)
                    )

                    TextField(
                        value = step,
                        onValueChange = { processSteps[index] = it },
                        placeholder = { Text("Step ${index + 1}") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.large
                    )

                    IconButton(
                        onClick = {
                            if (processSteps.size > 1) {
                                processSteps.removeAt(index)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete step",
                            tint = inkColor.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            item {
                TextButton(onClick = { processSteps.add("") }) {
                    Text("Add step")
                }
            }

            /* ---------- NOTES ---------- */

            item {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.titleMedium,
                    color = inkColor
                )
            }

            item {
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Optional notes…") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.large
                )
            }

            /* ---------- ACTIONS ---------- */

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Button(
                    onClick = {
                        scope.launch {
                            RecipeRepository.saveRecipeWithIngredients(
                                recipeId,
                                title.trim(),
                                processSteps.map { it.trim() }
                                    .filter { it.isNotEmpty() },
                                notes.trim(),
                                ingredients.map {
                                    ExportIngredient(
                                        name = it.name.trim(),
                                        quantity = it.quantity?.trim(),
                                        unit = it.unit?.trim(),
                                        notes = it.notes?.trim()
                                    )
                                }
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (recipeId == null) "Add Recipe" else "Save Changes")
                }
            }

            item {
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}
