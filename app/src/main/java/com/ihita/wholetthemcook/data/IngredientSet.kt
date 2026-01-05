package com.ihita.wholetthemcook.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Ingredient::class,
        parentColumns = ["id"],
        childColumns = ["ingredientId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class IngredientSet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val recipeId: Long,
    val ingredientId: Long,

    val quantity: Float? = null,
    val unit: String? = null,
    val notes: String? = null
)
