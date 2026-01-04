package com.ihita.wholetthemcook.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreRecipeService {

    private val db = FirebaseFirestore.getInstance()

    fun addRecipe(
        title: String,
        process: List<String>,
        notes: String?
    ) {
        val recipe = hashMapOf(
            "title" to title,
            "process" to process,
            "notes" to notes,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener {
                println("recipe uploaded to Firestore")
            }
            .addOnFailureListener {
                println("failed to upload recipe: $it")
            }
    }

    fun listenToAllRecipes(onUpdate: (List<Map<String, Any>>) -> Unit) {
        db.collection("recipes")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val recipes = snapshot.documents.mapNotNull { it.data }
                onUpdate(recipes)
            }
    }
}
