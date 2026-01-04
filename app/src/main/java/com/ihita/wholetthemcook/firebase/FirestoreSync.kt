package com.ihita.wholetthemcook.firebase

import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

import com.ihita.wholetthemcook.data.Ingredient
import com.ihita.wholetthemcook.data.IngredientSet
import com.ihita.wholetthemcook.data.Recipe

object FirestoreSync {

    private val db = FirebaseFirestore.getInstance()

    fun uploadRecipe(recipe: Recipe) {
        db.collection("recipes")
            .document(recipe.id.toString())
            .set(recipe)
    }

    fun uploadIngredient(ingredient: Ingredient) {
        db.collection("ingredients")
            .document(ingredient.id.toString())
            .set(ingredient)
    }

    fun uploadIngredientSet(set: IngredientSet) {
        db.collection("ingredientSets").add(
            mapOf(
                "recipeId" to set.recipeId,
                "ingredientId" to set.ingredientId,
                "quantity" to set.quantity,
                "unit" to set.unit,
                "notes" to set.notes
            )
        )
    }

    suspend fun deleteIngredientSetsForRecipe(recipeId: Long) {
        val snapshot = db.collection("ingredientSets")
            .whereEqualTo("recipeId", recipeId)
            .get()
            .await()

        snapshot.documents.forEach {
            it.reference.delete().await()
        }
    }


}
