package com.ihita.wholetthemcook.firebase

import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

import com.ihita.wholetthemcook.data.Ingredient
import com.ihita.wholetthemcook.data.IngredientSet
import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.firebase.model.FirestoreIngredientSet

object FirestoreSync {

    private val db = FirebaseFirestore.getInstance()

    suspend fun uploadRecipe(recipe: Recipe) {
        db.collection("recipes")
            .document(recipe.id.toString())
            .set(recipe)
            .await()
    }

    suspend fun uploadIngredient(ingredient: Ingredient) {
        db.collection("ingredients")
            .document(ingredient.id.toString())
            .set(ingredient)
            .await()
    }

    suspend fun uploadIngredientSet(set: FirestoreIngredientSet) {
        db.collection("ingredientSets")
            .document("${set.recipeId}_${set.ingredientId}")
            .set(
                mapOf(
                    "recipeId" to set.recipeId,
                    "ingredientId" to set.ingredientId,
                    "quantity" to set.quantity,
                    "unit" to set.unit,
                    "notes" to set.notes
                )
            )
            .await()
    }


    suspend fun deleteIngredientSetsForRecipe(recipeId: Long) {
        val snapshot = db.collection("ingredientSets")
            .whereEqualTo("recipeId", recipeId)
            .get()
            .await()

        if (snapshot.isEmpty) return

        val batch = db.batch()
        snapshot.documents.forEach { doc ->
            batch.delete(doc.reference)
        }

        batch.commit().await()

    }


    suspend fun deleteRecipe(recipeId: Long) {
        db.collection("recipes")
            .document(recipeId.toString())
            .delete()
            .await()
    }

}
