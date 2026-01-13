package com.ihita.wholetthemcook.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val dateAdded: Date = Date(),
    val dateOpened: Date = Date(),
    val process: List<String> = emptyList(),
    val notes: String? = null,
    val imageUri: String? = null,
    val extraImages: List<String>? = null
)
