package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.util.Date

import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.data.RecipeRepository
import com.ihita.wholetthemcook.ui.components.IngredientInput
import com.ihita.wholetthemcook.ui.components.IngredientRow

@Composable
fun AddEditRecipeScreen(navController: NavController, recipeId: Long? = null) {
    val scope = rememberCoroutineScope()

    // State for form fields
    var title by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf<IngredientInput>() }
    var processSteps = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }

    // Load existing recipe if editing
    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            val recipe = Database.recipeDao.getRecipeById(recipeId)
            title = recipe.title
            processSteps.clear()
            processSteps.addAll(recipe.process.ifEmpty { listOf("") })
            notes = recipe.notes ?: ""

            val ingredientRecipes = Database.ingredientSetDao.getIngredientsForRecipe(recipeId)
            ingredients.clear()
            ingredients.addAll(ingredientRecipes.map { ir ->
                    IngredientInput(
                        name = ir.ingredient.title,
                        quantity = ir.ingredientSet.quantity?.toString() ?: "",
                        unit = ir.ingredientSet.unit ?: "",
                        notes = ir.ingredientSet.notes ?: ""
                    )
                }
            )
            if (ingredients.isEmpty()) {
                ingredients.add(IngredientInput(name =  "", quantity = "", unit = "", notes = ""))
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ingredients.forEach { ingredient ->
            IngredientRow(
                ingredient = ingredient,
                onChange = { updated ->
                    val index = ingredients.indexOfFirst { it.id == updated.id }
                    if (index != -1) {
                        ingredients[index] = updated
                    }
                },
                onDelete = {
                    if (ingredients.size > 1) {
                        ingredients.remove(ingredient)
                    }
                }
            )
        }
        TextButton(onClick = { ingredients.add(IngredientInput()) }) {
            Text("+ Add ingredient")
        }

        Text("Process", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            itemsIndexed(processSteps) { index, step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("${index + 1}.")

                    TextField(
                        value = step,
                        onValueChange = { processSteps[index] = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Step ${index + 1}") }
                    )

                    IconButton(
                        onClick = {
                            if (processSteps.size > 1) {
                                processSteps.removeAt(index)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete step")
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }

        Button(
            onClick = { processSteps.add("") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Step")
        }

        Spacer(Modifier.height(16.dp))

        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    RecipeRepository.saveRecipeWithIngredients(recipeId, title, processSteps.map { it.trim() }.filter { it.isNotEmpty() }, notes, ingredients.map { IngredientInput(name = it.name.trim(), quantity = it.quantity.trim(), unit = it.unit.trim(), notes = it.notes.trim()) })
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("recipe_updated", true)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (recipeId == null) "Add Recipe" else "Save Changes")
        }

        Button(onClick = { navController.popBackStack() }) {
            Text("Cancel")
        }
    }
}
