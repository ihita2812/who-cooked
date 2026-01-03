package com.ihita.wholetthemcook.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun IngredientRow(ingredient: IngredientInput, onChange: (IngredientInput) -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        TextField(
            value = ingredient.name,
            onValueChange = { onChange(ingredient.copy(name = it)) },
            label = { Text("Ingredient") },
            modifier = Modifier.weight(0.3f),
            singleLine = true
        )

        TextField(
            value = ingredient.quantity,
            onValueChange = { onChange(ingredient.copy(quantity = it)) },
            label = { Text("Qty") },
            modifier = Modifier.weight(0.15f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        TextField(
            value = ingredient.unit,
            onValueChange = { onChange(ingredient.copy(unit = it)) },
            label = { Text("Unit") },
            modifier = Modifier.weight(0.15f),
            singleLine = true
        )

        TextField(
            value = ingredient.notes,
            onValueChange = { onChange(ingredient.copy(notes = it)) },
            label = { Text("Notes") },
            modifier = Modifier.weight(0.3f),
            singleLine = true
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier.weight(0.1f)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete ingredient"
            )
        }
    }
}
