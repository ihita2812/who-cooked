package com.ihita.wholetthemcook

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
//import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.ui.theme.WhoLetThemCookTheme
import com.ihita.wholetthemcook.navigation.WhoLetThemCookNavGraph
import com.ihita.wholetthemcook.data.Database
//import com.ihita.wholetthemcook.data.DummyDataSeeder
import com.ihita.wholetthemcook.firebase.FirebaseAuthManager
import com.ihita.wholetthemcook.firebase.FirestoreListeners

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        lifecycleScope.launch {
//            DummyDataSeeder.seed(Database)
//        }

        enableEdgeToEdge()
        setContent {
            WhoLetThemCookTheme {
                WhoLetThemCookNavGraph()
            }
        }

        FirebaseApp.initializeApp(this)

        FirebaseAuthManager.signInAnonymously { success, _ ->
            if (success) {
                Database.init(applicationContext)
                FirestoreListeners.startListening()
            } else {
                Log.e("MainActivity", "Anonymous sign-in failed, listeners not started")
            }
        }
    }


    override fun onDestroy() {
        FirestoreListeners.stopListening()
        super.onDestroy()
    }

}
