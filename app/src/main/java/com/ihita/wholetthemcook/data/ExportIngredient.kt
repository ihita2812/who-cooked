package com.ihita.wholetthemcook.data

import java.util.UUID

data class ExportIngredient(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val quantity: String? = null,
    val unit: String? = null,
    val notes: String? = null
)