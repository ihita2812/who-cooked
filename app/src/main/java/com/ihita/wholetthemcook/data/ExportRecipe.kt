package com.ihita.wholetthemcook.data

data class ExportRecipe(
    val title: String,
    val process: List<String>,
    val notes: String?,
    val ingredients: List<ExportIngredient>
)
