package com.ihita.wholetthemcook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeInfoViewModelFactory(private val recipeId: Long) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeInfoViewModel(recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
