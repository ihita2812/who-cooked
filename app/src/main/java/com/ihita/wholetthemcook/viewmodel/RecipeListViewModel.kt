package com.ihita.wholetthemcook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

import com.ihita.wholetthemcook.data.Database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeListViewModel : ViewModel() {

    val recipes = Database.recipeDao
        .getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _selectedRecipeIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedRecipeIds: StateFlow<Set<Long>> = _selectedRecipeIds

    val isSelectionMode: StateFlow<Boolean> =
        selectedRecipeIds.map { it.isNotEmpty() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleSelection(recipeId: Long) {
        _selectedRecipeIds.update { current ->
            if (current.contains(recipeId)) current - recipeId
            else current + recipeId
        }
    }

    fun clearSelection() {
        _selectedRecipeIds.value = emptySet()
    }

    fun deleteSelectedRecipes() {
        viewModelScope.launch {
            _selectedRecipeIds.value.forEach { id ->
                Database.recipeDao.deleteById(id)
            }
            clearSelection()
        }
    }
}
