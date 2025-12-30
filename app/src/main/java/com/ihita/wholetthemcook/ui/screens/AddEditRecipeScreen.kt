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
import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.Recipe
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun AddEditRecipeScreen(navController: NavController, recipeId: Long? = null, onSave: () -> Unit = {}) {
    val scope = rememberCoroutineScope()

    // State for form fields
    var title by remember { mutableStateOf("") }
    // var ingredients by remember
    var process by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var dateCreated by remember { mutableStateOf(Date()) }
    var dateOpened by remember { mutableStateOf(Date()) }

    // Load existing recipe if editing
    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            val recipe = Database.recipeDao.getRecipeById(recipeId)
            title = recipe.title
            dateCreated = recipe.dateAdded
            dateOpened = Date()
            // ingredients
            process = recipe.process ?: ""
            notes = recipe.notes ?: ""
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
                    if (recipeId == null) {
                        Database.recipeDao.insertRecipe(
                            Recipe(title = title, process = process, notes = notes, dateAdded = dateCreated, dateOpened = dateOpened)
                        )
                    } else {
                        val updatedRecipe = Recipe(id = recipeId, title = title, process = process, notes = notes, dateAdded = dateCreated, dateOpened = dateOpened)
                        Database.recipeDao.updateRecipe(updatedRecipe)
                    }
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (recipeId == null) "Add Recipe" else "Save Changes")
        }

        // CANCEL BUTTON
        /* TODO */
//        Button(onClick = navController.) {
//            Text("Cancel")
//        }
    }
}
