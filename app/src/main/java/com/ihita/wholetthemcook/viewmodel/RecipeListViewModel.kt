package com.ihita.wholetthemcook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.ihita.wholetthemcook.data.Database.recipeDao
import com.ihita.wholetthemcook.data.Recipe
import com.ihita.wholetthemcook.ui.components.SortOption
import kotlinx.coroutines.flow.combine

class RecipeListViewModel : ViewModel() {

    private val _selectedRecipeIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedRecipeIds: StateFlow<Set<Long>> = _selectedRecipeIds

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _sortOption = MutableStateFlow(SortOption.DATE_ADDED)
    val sortOption: StateFlow<SortOption> = _sortOption


    val isSelectionMode: StateFlow<Boolean> = selectedRecipeIds.map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val recipes: StateFlow<List<Recipe>> = combine(recipeDao.getAllRecipes(), searchQuery, sortOption) { recipes, query, sort ->

        val filtered = if (query.isBlank()) {
            recipes
        } else {
            recipes.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }

        when (sort) {
            SortOption.DATE_ADDED ->
                filtered.sortedByDescending { it.dateAdded }

            SortOption.DATE_MODIFIED ->
                filtered.sortedByDescending { it.dateOpened }

            SortOption.TITLE_ASC ->
                filtered.sortedBy { it.title.lowercase() }

            SortOption.TITLE_DESC ->
                filtered.sortedByDescending { it.title.lowercase() }
        }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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
                recipeDao.deleteById(id)
            }
            clearSelection()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

}
