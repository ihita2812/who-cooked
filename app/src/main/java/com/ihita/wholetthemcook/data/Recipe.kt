package com.ihita.wholetthemcook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val process: String? = null,
    val notes: String? = null
)
