package com.ihita.wholetthemcook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.data.Database
import com.ihita.wholetthemcook.data.ExportRecipe
import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.data.RecipeRepository
import com.ihita.wholetthemcook.ui.components.SortOption

class RecipeListViewModel : ViewModel() {

    private val _selectedRecipeIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedRecipeIds: StateFlow<Set<Long>> = _selectedRecipeIds

    val isSelectionMode: StateFlow<Boolean> = selectedRecipeIds.map { it.isNotEmpty() }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
                RecipeRepository.deleteRecipe(id)
            }
            clearSelection()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _sortOption = MutableStateFlow(SortOption.DATE_ADDED)
    val sortOption: StateFlow<SortOption> = _sortOption

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

    val recipes: StateFlow<List<Recipe>> = combine(
        Database.recipeDao.getAllRecipes(),
        _searchQuery,
        _sortOption
    ) { recipes, query, sort ->
        val filtered = if (query.isBlank()) recipes
        else recipes.filter { it.title.contains(query, ignoreCase = true) }
        when (sort) {
            SortOption.DATE_ADDED -> filtered.sortedByDescending { it.dateAdded }
            SortOption.DATE_MODIFIED -> filtered.sortedByDescending { it.dateOpened }
            SortOption.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
            SortOption.TITLE_DESC -> filtered.sortedByDescending { it.title.lowercase() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    suspend fun getSelectedExportRecipes(): List<ExportRecipe> {
        return _selectedRecipeIds.value.map { id ->
            val recipe = Database.recipeDao.getRecipeById(id)
            val ingredientSets = Database.ingredientSetDao.getIngredientsForRecipe(id)
            ExportRecipe(
                title = recipe.title,
                process = recipe.process,
                notes = recipe.notes,
                ingredients = ingredientSets.map {
                    com.ihita.wholetthemcook.data.ExportIngredient(
                        name = it.ingredient.title,
                        quantity = it.ingredientSet.quantity.toString(),
                        unit = it.ingredientSet.unit ?: "",
                        notes = it.ingredientSet.notes ?: ""
                    )
                }
            )
        }
    }
}
