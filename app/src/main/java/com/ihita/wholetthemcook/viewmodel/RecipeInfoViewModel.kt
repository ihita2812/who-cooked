package com.ihita.wholetthemcook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.ExportIngredient
import com.ihita.wholetthemcook.data.ExportRecipe
import com.ihita.wholetthemcook.data.IngredientRecipe
import com.ihita.wholetthemcook.data.Recipe
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecipeInfoViewModel(private val recipeId: Long) : ViewModel() {

    val recipe: StateFlow<Recipe?> = Database.recipeDao.getRecipeByIdFlow(recipeId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val ingredients: StateFlow<List<IngredientRecipe>> = Database.ingredientSetDao.getIngredientsForRecipeFlow(recipeId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted

    fun deleteRecipe() {
        viewModelScope.launch {
            Database.recipeDao.deleteById(recipeId)
            _isDeleted.value = true
        }
    }

    fun getExportRecipe(): ExportRecipe? {
        val r = recipe.value ?: return null
        return ExportRecipe(
            title = r.title,
            process = r.process,
            notes = r.notes,
            ingredients = ingredients.value.map {
                ExportIngredient(
                    name = it.ingredient.title,
                    quantity = it.ingredientSet.quantity.toString(),
                    unit = it.ingredientSet.unit ?: "",
                    notes = it.ingredientSet.notes ?: ""
                )
            }
        )
    }
}
