package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ihita.wholetthemcook.data.Recipe

@Composable
fun RecipeListScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text("Recipe List (coming soon!)")
//    }

    val dummyRecipes = listOf(
        Recipe(1, "adiddy"),
        Recipe(2, "nigesh"),
        Recipe(3, "idli")
    )

    LazyColumn {
        items(dummyRecipes) { recipe ->
            Text(
                text = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
