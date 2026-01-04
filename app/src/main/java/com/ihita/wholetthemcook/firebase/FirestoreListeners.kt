package com.ihita.wholetthemcook.firebase

import com.google.firebase.firestore.*
import com.ihita.wholetthemcook.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FirestoreListeners {

    private val db = FirebaseFirestore.getInstance()
    private var recipesListener: ListenerRegistration? = null
    private var ingredientsListener: ListenerRegistration? = null
    private var ingredientSetsListener: ListenerRegistration? = null

    fun startListening() {
        listenRecipes()
        listenIngredients()
        listenIngredientSets()
    }

    fun stopListening() {
        recipesListener?.remove()
        ingredientsListener?.remove()
        ingredientSetsListener?.remove()
    }

    // ------------------ Recipes ------------------
    private fun listenRecipes() {
        recipesListener = db.collection("recipes")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                CoroutineScope(Dispatchers.IO).launch {
                    snapshot.documentChanges.forEach { change ->
                        val recipe = change.document.toObject(Recipe::class.java)
                        when (change.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                Database.recipeDao.insertOrUpdate(recipe)
                            }
                            DocumentChange.Type.REMOVED -> {
                                Database.recipeDao.deleteById(recipe.id)
                            }
                        }
                    }
                }
            }
    }

    // ------------------ Ingredients ------------------
    private fun listenIngredients() {
        ingredientsListener = db.collection("ingredients")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                CoroutineScope(Dispatchers.IO).launch {
                    snapshot.documentChanges.forEach { change ->
                        val ingredient = change.document.toObject(Ingredient::class.java)
                        when (change.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                Database.ingredientDao.insertOrUpdate(ingredient)
                            }
                            DocumentChange.Type.REMOVED -> {
                                ingredient.id?.let { Database.ingredientDao.deleteById(it) }
                            }
                        }
                    }
                }
            }
    }

    // ------------------ IngredientSets ------------------
    private fun listenIngredientSets() {
        ingredientSetsListener = db.collection("ingredientSets")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                CoroutineScope(Dispatchers.IO).launch {
                    snapshot.documentChanges.forEach { change ->
                        val set = change.document.toObject(IngredientSet::class.java)
                        when (change.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                Database.ingredientSetDao.insertOrUpdate(set)
                            }
                            DocumentChange.Type.REMOVED -> {
                                // Room DAO method to delete by recipeId + ingredientId
                                Database.ingredientSetDao.deleteByRecipeAndIngredient(set.recipeId, set.ingredientId)
                            }
                        }
                    }
                }
            }
    }
}
