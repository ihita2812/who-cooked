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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

import com.ihita.wholetthemcook.data.ExportIngredient

@Composable
fun IngredientRow(
    ingredient: ExportIngredient,
    onChange: (ExportIngredient) -> Unit,
    onDelete: () -> Unit
) {
    val fieldHeight = 56.dp
    val inkColor = Color(0xFF3E3A36)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Ingredient name (largest)
        TextField(
            value = ingredient.name,
            onValueChange = {
                onChange(ingredient.copy(name = it))
            },
            placeholder = { Text("Ingredient") },
            modifier = Modifier
                .weight(2.2f)
                .height(fieldHeight),
            singleLine = true
        )

        // Quantity (small, strict)
        TextField(
            value = ingredient.quantity ?: "",
            onValueChange = {
                if (it.length <= 6) {   // prevents stupid overflow
                    onChange(ingredient.copy(quantity = it))
                }
            },
            placeholder = { Text("Qty") },
            modifier = Modifier
                .weight(0.8f)
                .height(fieldHeight),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        // Unit (small)
        TextField(
            value = ingredient.unit ?: "",
            onValueChange = {
                onChange(ingredient.copy(unit = it))
            },
            placeholder = { Text("Unit") },
            modifier = Modifier
                .weight(0.9f)
                .height(fieldHeight),
            singleLine = true
        )

        // Notes (medium)
        TextField(
            value = ingredient.notes ?: "",
            onValueChange = {
                onChange(ingredient.copy(notes = it))
            },
            placeholder = { Text("Notes") },
            modifier = Modifier
                .weight(1.4f)
                .height(fieldHeight),
            singleLine = true
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete ingredient",
                tint = inkColor.copy(alpha = 0.6f)
            )
        }
    }
}
