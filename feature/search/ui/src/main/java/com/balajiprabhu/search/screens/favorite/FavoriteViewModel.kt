package com.balajiprabhu.search.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.useCases.GetAllSavedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllSavedRecipesUseCase: GetAllSavedRecipesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteWrapper.UiState(isLoading = true))
    val uiState: StateFlow<FavoriteWrapper.UiState> get() = _uiState.asStateFlow()

    private val _navigation = Channel<FavoriteWrapper.Navigation>()
    val navigation get() = _navigation.receiveAsFlow()

    fun onEvent(event: FavoriteWrapper.Event) {
        when (event) {
            is FavoriteWrapper.Event.FetchAllSavedRecipes -> {
                getAllSavedRecipes()
            }

            FavoriteWrapper.Event.OnBackClick -> {
                viewModelScope.launch {
                    _navigation.send(FavoriteWrapper.Navigation.PopBackStack)
                }
            }

            is FavoriteWrapper.Event.NavigateRecipeDetail -> {
                viewModelScope.launch {
                    _navigation.send(FavoriteWrapper.Navigation.RecipeDetails(event.id))
                }
            }
        }
    }

    private fun getAllSavedRecipes() = viewModelScope.launch {
        getAllSavedRecipesUseCase().collectLatest { recipeList ->

            if (recipeList.isEmpty()) {
                _uiState.update {
                    FavoriteWrapper.UiState(isEmpty = true)
                }
            }

            if (recipeList.isNotEmpty()) {
                _uiState.update {
                    FavoriteWrapper.UiState(recipes = recipeList)
                }
            }

        }
    }

}

object FavoriteWrapper {

    data class UiState(
        val isLoading: Boolean = false,
        val recipes: List<Recipe>? = null,
        val isEmpty: Boolean = false,
    )

    sealed class Navigation {
        data object PopBackStack : Navigation()
        data class RecipeDetails(val id: String) : Navigation()
    }

    sealed class Event {
        data object FetchAllSavedRecipes : Event()
        data object OnBackClick : Event()
        data class NavigateRecipeDetail(val id:String): Event()
    }
}