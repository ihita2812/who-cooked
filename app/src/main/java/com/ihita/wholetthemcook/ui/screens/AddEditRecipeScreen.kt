package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
    var process by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Load existing recipe if editing
    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            val recipe = Database.recipeDao.getRecipeById(recipeId)
            title = recipe.title
            process = recipe.process ?: ""
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

//        Spacer(modifier = Modifier.height(24.dp))
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

        TextField(
            value = process,
            onValueChange = { process = it },
            label = { Text("Process") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

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
                    RecipeRepository.saveRecipeWithIngredients(recipeId, title, process, notes, ingredients.map { IngredientInput(name = it.name.trim(), quantity = it.quantity.trim(), unit = it.unit.trim(), notes = it.notes.trim()) })
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
