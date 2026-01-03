package com.ihita.wholetthemcook.ui.components

import java.util.UUID

data class IngredientInput(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val quantity: String = "",
    val unit: String = "",
    val notes: String = ""
)
