package com.ihita.wholetthemcook

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
//import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.ui.theme.WhoLetThemCookTheme
import com.ihita.wholetthemcook.navigation.WhoLetThemCookNavGraph
import com.ihita.wholetthemcook.data.Database
//import com.ihita.wholetthemcook.data.DummyDataSeeder
import com.ihita.wholetthemcook.firebase.FirebaseAuthManager
import com.ihita.wholetthemcook.firebase.FirestoreListeners
import java.lang.Exception

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            enableEdgeToEdge()
            setContent {
                WhoLetThemCookTheme {
                    WhoLetThemCookNavGraph()
                }
            }

            FirebaseApp.initializeApp(this)
////            FirebaseFirestore.getInstance().clearPersistence()
//            val settings = FirebaseFirestoreSettings.Builder()
//                .setLocalCacheSettings(
//                    MemoryCacheSettings.newBuilder()
//                        .build()
//                )
//                .build()
//            FirebaseFirestore.getInstance().firestoreSettings = settings


            FirebaseAuthManager.signInAnonymously { success, _ ->
                if (success) {
                    Database.init(applicationContext)
                    FirestoreListeners.startListening()
                } else {
                    Log.e("MainActivity", "Anonymous sign-in failed, listeners not started")
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivityCrash", "Exception in onCreate", e)
        }
    }


    override fun onDestroy() {
        FirestoreListeners.stopListening()
        super.onDestroy()
    }

}
