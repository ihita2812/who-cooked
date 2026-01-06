package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import com.ihita.wholetthemcook.navigation.Routes
import com.ihita.wholetthemcook.ui.components.PaperScreen

@Composable
fun HomeScreen(navController: NavHostController) {

    PaperScreen {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Who Cooked?",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "for the recipes of 401",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(Routes.RECIPE_LIST) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.extraLarge,
                contentPadding = PaddingValues(
                    horizontal = 28.dp,
                    vertical = 14.dp
                )
            ) {
                Text("View recipes")
            }
        }
    }
}
