package com.ihita.wholetthemcook.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity (indices = [Index(value = ["title"], unique = true)])
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String
)
