package com.ihita.wholetthemcook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeInfoViewModel(private val recipeId: Long) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            _recipe.value = Database.recipeDao.getRecipeById(recipeId)
        }
    }
}
