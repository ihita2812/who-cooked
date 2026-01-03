package com.ihita.wholetthemcook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.IngredientRecipe
import com.ihita.wholetthemcook.data.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeInfoViewModel(private val recipeId: Long) : ViewModel() {

    val recipe: StateFlow<Recipe?> =
        Database.recipeDao.getRecipeByIdFlow(recipeId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted

    private val _ingredients = MutableStateFlow<List<IngredientRecipe>>(emptyList())
    val ingredients: StateFlow<List<IngredientRecipe>> = _ingredients

    fun deleteRecipe() {
        viewModelScope.launch {
            Database.recipeDao.deleteById(recipeId)
            _isDeleted.value = true
        }
    }

    fun reload() {
        viewModelScope.launch {
            _ingredients.value = Database.ingredientSetDao.getIngredientsForRecipe(recipeId)
        }
    }

    init {
//        loadRecipe()
        reload()
    }

}
