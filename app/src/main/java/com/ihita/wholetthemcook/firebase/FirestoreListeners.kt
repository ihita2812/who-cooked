package com.ihita.wholetthemcook.firebase

import com.google.firebase.firestore.*
import com.ihita.wholetthemcook.data.*
import com.ihita.wholetthemcook.firebase.model.FirestoreIngredientSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object FirestoreListeners {

    private val db = FirebaseFirestore.getInstance()
    private var recipesListener: ListenerRegistration? = null
    private var ingredientsListener: ListenerRegistration? = null
    private var ingredientSetsListener: ListenerRegistration? = null

    fun startListening() {
        listenRecipes()
        listenIngredients()
        // listenIngredientSets()

        // Delay IngredientSets slightly
        CoroutineScope(Dispatchers.IO).launch {
            delay(1500)
            listenIngredientSets()
        }
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
                                ingredient.id.let { Database.ingredientDao.deleteById(it) }
                            }
                        }
                    }
                }
            }
    }

    // ------------------ IngredientSets ------------------
    private fun listenIngredientSets() {
        ingredientSetsListener = db.collection("ingredientSets").addSnapshotListener { snapshot, error ->

            if (error != null || snapshot == null) return@addSnapshotListener

            CoroutineScope(Dispatchers.IO).launch {
                snapshot.documentChanges.forEach { change ->
//                    val set = change.document.toObject(IngredientSet::class.java)

                    val firestoreSet = change.document.toObject(FirestoreIngredientSet::class.java)
                    val roomSet = IngredientSet(
                        recipeId = firestoreSet.recipeId,
                        ingredientId = firestoreSet.ingredientId,
                        quantity = firestoreSet.quantity,
                        unit = firestoreSet.unit,
                        notes = firestoreSet.notes
                    )

                    when (change.type) {
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED -> { Database.ingredientSetDao.insertOrUpdate(roomSet) }
                        DocumentChange.Type.REMOVED -> { Database.ingredientSetDao.deleteByRecipeAndIngredient(roomSet.recipeId, roomSet.ingredientId) }
                    }
                }
            }
        }
    }

}
