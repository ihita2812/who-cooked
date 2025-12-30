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

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            _recipe.value = Database.recipeDao.getRecipeById(recipeId)
        }
    }

    fun deleteRecipe() {
        viewModelScope.launch {
            Database.recipeDao.deleteRecipeById(recipeId)
            _isDeleted.value = true
        }
    }
}
