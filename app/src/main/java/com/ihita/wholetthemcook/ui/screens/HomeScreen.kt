package com.ihita.wholetthemcook.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import com.ihita.wholetthemcook.R
import com.ihita.wholetthemcook.navigation.Routes
import com.ihita.wholetthemcook.ui.components.AnimateImage
import com.ihita.wholetthemcook.ui.components.PaperScreen
import com.ihita.wholetthemcook.ui.theme.*

@Composable
fun HomeScreen(navController: NavHostController) {

    PaperScreen {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier.wrapContentSize(),
                    contentAlignment = Alignment.Center
                ) {

                    AnimateImage(
                        painter = painterResource(R.drawable.niggesh),
                        isLeft = true,
                        imageSize = 180.dp,
                        spreadX = 250f,
                        riseY = 250f
                    )

                    AnimateImage(
                        painter = painterResource(R.drawable.adiddy),
                        isLeft = false,
                        imageSize = 180.dp,
                        spreadX = 250f,
                        riseY = 250f
                    )

                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Who Cooked?",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkPurple
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "FOR THE RECIPES OF 401",
                    style = MaterialTheme.typography.labelLarge,
                    color = LighterPurple
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { navController.navigate(Routes.RECIPE_LIST) },
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonPrimary
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 32.dp,
                        vertical = 14.dp
                    )
                ) {
                    Text(
                        text = "View recipes",
                        style = MaterialTheme.typography.labelSmall,
                        color = ButtonText
                    )
                }
            }
        }
    }
}
