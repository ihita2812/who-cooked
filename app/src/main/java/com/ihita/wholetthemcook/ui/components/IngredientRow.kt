package com.ihita.wholetthemcook.ui.components

import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.ihita.wholetthemcook.data.ExportIngredient
import com.ihita.wholetthemcook.ui.theme.*

@Composable
fun IngredientRow(ingredient: ExportIngredient, onChange: (ExportIngredient) -> Unit, onDelete: () -> Unit) {
    val fieldHeight = 56.dp

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = LightLilac.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = ingredient.name,
                    onValueChange = { onChange(ingredient.copy(name = it)) },
                    placeholder = { Text(
                        text = "Ingredient",
                        color = TextMuted
                    ) },
                    modifier = Modifier
                        .weight(2.2f)
                        .height(fieldHeight),
                    singleLine = true,
                    colors = ingredientFieldColors(),
                    shape = MaterialTheme.shapes.large,
//                    shape = RoundedCornerShape(
//                        topStart = 16.dp,
//                        topEnd = 16.dp,
//                        bottomStart = 0.dp,
//                        bottomEnd = 0.dp
//                    )

                )

                TextField(
                    value = ingredient.quantity ?: "",
                    onValueChange = {
                        if (it.length <= 6) {
                            onChange(ingredient.copy(quantity = it))
                        }
                    },
                    placeholder = { Text(
                        text = "Qty",
                        color = TextMuted
                    ) },
                    modifier = Modifier
                        .weight(0.8f)
                        .height(fieldHeight),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = ingredientFieldColors(),
                    shape = MaterialTheme.shapes.large,
//                    shape = RoundedCornerShape(
//                        topStart = 16.dp,
//                        topEnd = 16.dp,
//                        bottomStart = 0.dp,
//                        bottomEnd = 0.dp
//                    )
                )

                TextField(
                    value = ingredient.unit ?: "",
                    onValueChange = { onChange(ingredient.copy(unit = it)) },
                    placeholder = { Text(
                        text = "Unit",
                        color = TextMuted
                    ) },
                    modifier = Modifier
                        .weight(0.9f)
                        .height(fieldHeight),
                    singleLine = true,
                    colors = ingredientFieldColors(),
                    shape = MaterialTheme.shapes.large,
//                    shape = RoundedCornerShape(
//                        topStart = 16.dp,
//                        topEnd = 16.dp,
//                        bottomStart = 0.dp,
//                        bottomEnd = 0.dp
//                    )
                )

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = ingredient.notes ?: "",
                    onValueChange = { onChange(ingredient.copy(notes = it)) },
                    placeholder = { Text(
                        text = "Notes",
                        color = TextMuted
                    ) },
                    modifier = Modifier
                        .weight(1.4f)
                        .height(fieldHeight),
                    singleLine = true,
                    colors = ingredientFieldColors(),
                    shape = MaterialTheme.shapes.large,
//                    shape = RoundedCornerShape(
//                        topStart = 0.dp,
//                        topEnd = 0.dp,
//                        bottomStart = 16.dp,
//                        bottomEnd = 16.dp
//                    )
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete ingredient",
                        tint = TextBody
                    )
                }

            }

        }
    }

//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        TextField(
//            value = ingredient.name,
//            onValueChange = { onChange(ingredient.copy(name = it)) },
//            placeholder = { Text(
//                text = "Ingredient",
//                color = TextMuted
//            ) },
//            modifier = Modifier
//                .weight(2.2f)
//                .height(fieldHeight),
//            singleLine = true,
//            colors = ingredientFieldColors(),
////            shape = MaterialTheme.shapes.large,
//            shape = RoundedCornerShape(
//                topStart = 16.dp,
//                topEnd = 16.dp,
//                bottomStart = 0.dp,
//                bottomEnd = 0.dp
//            )
//
//        )
//
//        TextField(
//            value = ingredient.quantity ?: "",
//            onValueChange = {
//                if (it.length <= 6) {
//                    onChange(ingredient.copy(quantity = it))
//                }
//            },
//            placeholder = { Text(
//                text = "Qty",
//                color = TextMuted
//            ) },
//            modifier = Modifier
//                .weight(0.8f)
//                .height(fieldHeight),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number
//            ),
//            colors = ingredientFieldColors(),
////            shape = MaterialTheme.shapes.large,
//            shape = RoundedCornerShape(
//                topStart = 16.dp,
//                topEnd = 16.dp,
//                bottomStart = 0.dp,
//                bottomEnd = 0.dp
//            )
//        )
//
//        TextField(
//            value = ingredient.unit ?: "",
//            onValueChange = { onChange(ingredient.copy(unit = it)) },
//            placeholder = { Text(
//                text = "Unit",
//                color = TextMuted
//            ) },
//            modifier = Modifier
//                .weight(0.9f)
//                .height(fieldHeight),
//            singleLine = true,
//            colors = ingredientFieldColors(),
////            shape = MaterialTheme.shapes.large,
//            shape = RoundedCornerShape(
//                topStart = 16.dp,
//                topEnd = 16.dp,
//                bottomStart = 0.dp,
//                bottomEnd = 0.dp
//            )
//        )
//
//    }
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        TextField(
//            value = ingredient.notes ?: "",
//            onValueChange = { onChange(ingredient.copy(notes = it)) },
//            placeholder = { Text(
//                text = "Notes",
//                color = TextMuted
//            ) },
//            modifier = Modifier
//                .weight(1.4f)
//                .height(fieldHeight),
//            singleLine = true,
//            colors = ingredientFieldColors(),
////            shape = MaterialTheme.shapes.large,
//            shape = RoundedCornerShape(
//                topStart = 0.dp,
//                topEnd = 0.dp,
//                bottomStart = 16.dp,
//                bottomEnd = 16.dp
//            )
//        )
//
//        IconButton(
//            onClick = onDelete,
//            modifier = Modifier.size(40.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Delete,
//                contentDescription = "Delete ingredient",
//                tint = TextBody
//            )
//        }
//
//    }
    
}

@Composable
private fun ingredientFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = LightLilac,
    unfocusedContainerColor = LightLilac.copy(alpha = 0.85f),
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    cursorColor = DarkPurple,
    focusedTextColor = TextBody,
    unfocusedTextColor = TextBody
)
