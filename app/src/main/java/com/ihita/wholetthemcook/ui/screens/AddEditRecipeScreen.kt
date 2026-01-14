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
import com.ihita.wholetthemcook.ui.theme.*

@Composable
fun AddEditRecipeScreen(navController: NavController, recipeId: Long? = null) {

    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }
    val ingredients = remember { mutableStateListOf<ExportIngredient>() }
    val processSteps = remember { mutableStateListOf("") }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            val recipe = Database.recipeDao.getRecipeById(recipeId)
            title = recipe.title
            notes = recipe.notes.orEmpty()

            processSteps.clear()
            processSteps.addAll(recipe.process.ifEmpty { listOf("") })

            ingredients.clear()
            ingredients.addAll(
                Database.ingredientSetDao
                    .getIngredientsForRecipe(recipeId)
                    .map {
                        ExportIngredient(
                            name = it.ingredient.title,
                            quantity = it.ingredientSet.quantity?.toString(),
                            unit = it.ingredientSet.unit,
                            notes = it.ingredientSet.notes
                        )
                    }
            )

            if (ingredients.isEmpty()) ingredients.add(ExportIngredient())
        } else {
            ingredients.add(ExportIngredient())
        }
    }

    val isSaveEnabled = title.trim().isNotEmpty()

    PaperScreen {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {

            item {
                Text(
                    text = if (recipeId == null) "New Recipe" else "Edit Recipe",
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

            item {
                LabeledField(label = "RECIPE TITLE") {
                    TextField(
                        value = title,
                        onValueChange = {
                            title = it
                            titleError = false
                        },
                        isError = titleError,
                        placeholder = {
                            Text(
                                text = "e.g. Lemon Tart",
                                color = TextMuted
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = textFieldColors(),
                        shape = MaterialTheme.shapes.large
                    )

                    if (titleError) {
                        Text(
                            text = "Recipe title is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            item { SectionHeader(text = "INGREDIENTS") }

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
                            },
//                            onDelete = {
//                                val removed = ingredient
//                                ingredients.remove(ingredient)
//
//                                scope.launch {
//                                    snackbarHostState.showSnackbar(
//                                        message = "${removed.name} removed",
//                                        actionLabel = "Undo"
//                                    ).let { result ->
//                                        if (result == SnackbarResult.ActionPerformed) {
//                                            ingredients.add(removed)
//                                        }
//                                    }
//                                }
//                            }
                        )
                    }
                }
            }

            item {
                TextButton(onClick = { ingredients.add(ExportIngredient()) }) {
                    Text(
                        text = "Add ingredient",
                        style = MaterialTheme.typography.labelSmall,
                        color = LighterPurple
                    )
                }
            }

            item { SectionHeader(text = "PROCESS") }

            itemsIndexed(processSteps) { index, step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${index + 1}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBody,
                        modifier = Modifier.width(26.dp)
                    )

                    TextField(
                        value = step,
                        onValueChange = { processSteps[index] = it },
                        placeholder = {
                            Text(
                                text = "Step ${index + 1}",
                                color = TextMuted
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = textFieldColors(),
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
                            tint = TextBody
                        )
                    }
                }
            }

            item {
                TextButton(onClick = { processSteps.add("") }) {
                    Text(
                        text = "Add step",
                        style = MaterialTheme.typography.labelSmall,
                        color = LighterPurple
                    )
                }
            }

            item { SectionHeader(text = "NOTES") }

            item {
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = {
                        Text(
                            text = "Optional notes…",
                            color = TextMuted
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors(),
                    shape = MaterialTheme.shapes.large,
                    minLines = 3
                )
            }

            item {
                Button(
                    onClick = {
                        val trimmedTitle = title.trim()
                        if (trimmedTitle.isEmpty()) {
                            titleError = true
                            return@Button
                        }

                        scope.launch {
                            RecipeRepository.saveRecipeWithIngredients(
                                recipeId = recipeId,
                                title = trimmedTitle,
                                process = processSteps
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() },
                                notes = notes.trim(),
                                ingredients = ingredients
                                    .mapNotNull { ing ->
                                        val qty = ing.quantity
                                            ?.trim()
                                            ?.toFloatOrNull()

                                        if (
                                            ing.name.isBlank() &&
                                            qty == null &&
                                            ing.unit.isNullOrBlank() &&
                                            ing.notes.isNullOrBlank()
                                        ) {
                                            null
                                        } else {
                                            ExportIngredient(
                                                name = ing.name.trim(),
                                                quantity = qty?.toString(),
                                                unit = ing.unit?.trim(),
                                                notes = ing.notes?.trim()
                                            )
                                        }
                                    }
                            )
                            navController.popBackStack()
                        }
                    },
                    enabled = isSaveEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonPrimary,
                        disabledContainerColor = ButtonPrimary.copy(alpha = 0.4f)
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = if (recipeId == null) "Add Recipe" else "Save Changes",
                        style = MaterialTheme.typography.labelSmall,
                        color = ButtonText
                    )
                }
            }

            item {
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextBody
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = LighterPurple
    )
}

@Composable
private fun LabeledField(
    label: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = LighterPurple
        )
        content()
    }
}

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = LightLilac,
    unfocusedContainerColor = LightLilac.copy(alpha = 0.85f),
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    cursorColor = DarkPurple,
    focusedTextColor = TextBody,
    unfocusedTextColor = TextBody
)
