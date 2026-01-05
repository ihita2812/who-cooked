package com.ihita.wholetthemcook.firebase.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirestoreIngredientSet(
    var id: Long = 0L,
    var recipeId: Long = 0L,
    var ingredientId: Long = 0L,
    var quantity: Float? = null,
    var unit: String? = null,
    var notes: String? = null
)
