package com.ihita.wholetthemcook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.ui.theme.WhoLetThemCookTheme
import com.ihita.wholetthemcook.navigation.WhoLetThemCookNavGraph
import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.DummyDataSeeder

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.init(applicationContext)

        lifecycleScope.launch {
            DummyDataSeeder.seed(Database)
        }

        enableEdgeToEdge()
        setContent {
            WhoLetThemCookTheme() {
                WhoLetThemCookNavGraph()
            }
        }
    }
}
