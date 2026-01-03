package com.ihita.wholetthemcook.data

import androidx.room.Embedded
import androidx.room.Relation

data class IngredientRecipe(
    @Embedded val ingredientSet: IngredientSet,

    @Relation(
        parentColumn = "ingredientId",
        entityColumn = "id"
    )
    val ingredient: Ingredient
)
