package com.balajiprabhu.search.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balajiprabhu.common.utils.UiText
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.useCases.DeleteRecipeUseCase
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
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteWrapper.UiState())
    val uiState: StateFlow<FavoriteWrapper.UiState> get() = _uiState.asStateFlow()

    private val _navigation = Channel<FavoriteWrapper.Navigation>()
    val navigation get() = _navigation.receiveAsFlow()

    private val initialRecipeList = mutableListOf<Recipe>()

    init {
        getAllSavedRecipes()
    }

    fun onEvent(event: FavoriteWrapper.Event) {
        when (event) {
            is FavoriteWrapper.Event.FetchAllSavedRecipes -> {
                getAllSavedRecipes()
            }

            is FavoriteWrapper.Event.OnBackClick -> {
                viewModelScope.launch {
                    _navigation.send(FavoriteWrapper.Navigation.PopBackStack)
                }
            }

            is FavoriteWrapper.Event.NavigateRecipeDetail -> {
                viewModelScope.launch {
                    _navigation.send(FavoriteWrapper.Navigation.RecipeDetails(event.id))
                }
            }

            is FavoriteWrapper.Event.AlphabeticalSort -> alphabeticSort()

            is FavoriteWrapper.Event.LessIngredients -> lessIngredientsSort()

            is FavoriteWrapper.Event.ResetSort -> resetSort()

            is FavoriteWrapper.Event.DeleteRecipe -> {
                deleteRecipe(event.recipe)
            }
        }
    }

    private fun getAllSavedRecipes() = viewModelScope.launch {
        getAllSavedRecipesUseCase.invoke().collectLatest { recipeList ->

            if (recipeList.isEmpty()) {
                _uiState.update {
                    FavoriteWrapper.UiState(error = UiText.RemoteString("Something went wrong"))
                }
            }

            if (recipeList.isNotEmpty()) {
                _uiState.update {
                    initialRecipeList.clear()
                    initialRecipeList.addAll(recipeList.toMutableList())
                    FavoriteWrapper.UiState(recipes = recipeList)
                }
            }
        }
    }

    private fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            deleteRecipeUseCase.invoke(recipe).collectLatest {
                getAllSavedRecipes()
            }
        }
    }

    private fun alphabeticSort() {
        _uiState.update {
            FavoriteWrapper.UiState(recipes = initialRecipeList.sortedBy { it.meal })
        }
    }

    private fun lessIngredientsSort() {
        _uiState.update {
            FavoriteWrapper.UiState(recipes = initialRecipeList.sortedBy { it.instructions.length })
        }
    }

    private fun resetSort() {
        _uiState.update {
            FavoriteWrapper.UiState(recipes = initialRecipeList)
        }
    }

}

object FavoriteWrapper {

    data class UiState(
        val isLoading: Boolean = false,
        val recipes: List<Recipe>? = null,
        val error: UiText = UiText.Idle,
    )

    sealed interface Navigation {
        data object PopBackStack : Navigation
        data class RecipeDetails(val id: String) : Navigation
    }

    sealed interface Event {
        data object AlphabeticalSort: Event
        data object LessIngredients: Event
        data object ResetSort: Event
        data object FetchAllSavedRecipes: Event
        data object OnBackClick: Event
        data class NavigateRecipeDetail(val id:String): Event
        data class DeleteRecipe(val recipe: Recipe): Event
    }
}