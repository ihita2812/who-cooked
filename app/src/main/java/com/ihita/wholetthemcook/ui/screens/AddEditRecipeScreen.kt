package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.R
import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.RecipeRepository
import com.ihita.wholetthemcook.data.ExportIngredient
import com.ihita.wholetthemcook.ui.components.IngredientRow

@Composable
fun AddEditRecipeScreen(
    navController: NavController,
    recipeId: Long? = null
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf<ExportIngredient>() }
    val processSteps = remember { mutableStateListOf("") }
    var notes by remember { mutableStateOf("") }

    val inkColor = Color(0xFF3E3A36)

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

    Box(modifier = Modifier.fillMaxSize()) {

        // Paper background
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
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Screen title
            item {
                Text(
                    text = if (recipeId == null) "New Recipe" else "Edit Recipe",
                    style = MaterialTheme.typography.headlineMedium,
                    color = inkColor
                )
            }

            // Recipe title input
            item {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Recipe title") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            // Ingredients
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

            // Process
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                        modifier = Modifier.weight(1f)
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

            // Notes
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
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Actions
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
