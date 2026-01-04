package com.ihita.wholetthemcook.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.util.Log

object FirebaseAuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInAnonymously(callback: (success: Boolean, user: FirebaseUser?) -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.i("FirebaseAuthManager", "Anonymous sign-in successful. UID=${user?.uid}")
                    callback(true, user)
                } else {
                    Log.e("FirebaseAuthManager", "Anonymous sign-in failed", task.exception)
                    callback(false, null)
                }
            }
    }
}
