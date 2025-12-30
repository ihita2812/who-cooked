package com.ihita.wholetthemcook.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListTopBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    var showSortMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search recipes") },
                singleLine = true,
                modifier = Modifier,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        },
        actions = {
            IconButton(onClick = { showSortMenu = true }) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "Sort"
                )
            }

            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Date added") },
                    onClick = {
                        onSortSelected(SortOption.DATE_ADDED)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Date modified") },
                    onClick = {
                        onSortSelected(SortOption.DATE_MODIFIED)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("A → Z") },
                    onClick = {
                        onSortSelected(SortOption.TITLE_ASC)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Z → A") },
                    onClick = {
                        onSortSelected(SortOption.TITLE_DESC)
                        showSortMenu = false
                    }
                )
            }
        }
    )
}
