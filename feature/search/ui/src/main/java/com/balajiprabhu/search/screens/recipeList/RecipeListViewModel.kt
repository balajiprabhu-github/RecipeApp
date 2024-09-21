package com.balajiprabhu.search.screens.recipeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.balajiprabhu.common.utils.NetworkResult
import com.balajiprabhu.common.utils.UiText
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.useCases.GetAllRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getAllRecipesUseCase: GetAllRecipesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeList.UiState())
    val uiState: StateFlow<RecipeList.UiState> = _uiState.asStateFlow()

    private val _navigation = Channel<RecipeList.Navigation>()
    val navigation: Flow<RecipeList.Navigation> = _navigation.receiveAsFlow()

    fun onEvent(event: RecipeList.Event) {
        when(event) {
            is RecipeList.Event.Search -> {
                search(event.q)
            }
            is RecipeList.Event.NavigateToRecipeDetails -> {
                viewModelScope.launch {
                    _navigation.send(RecipeList.Navigation.RecipeDetails(event.id))
                }
            }
            is RecipeList.Event.None -> {}
            is RecipeList.Event.NavigateToFavorite -> {
                viewModelScope.launch {
                    _navigation.send(RecipeList.Navigation.Favorite)
                }
            }
        }
    }

    private fun search(q: String) =
        getAllRecipesUseCase(q).onEach { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.update {
                            RecipeList.UiState(isLoading = true)
                        }
                    }

                    is NetworkResult.Success -> {
                        _uiState.update {
                            RecipeList.UiState(recipes = result.data)
                        }
                    }

                    is NetworkResult.Error -> {
                        _uiState.update {
                            RecipeList.UiState(
                                error = UiText.RemoteString(
                                    result.error ?: "Something went wrong"
                                )
                            )
                        }
                    }
                }
    }.launchIn(viewModelScope)
}

object RecipeList {

    data class UiState(
        val isLoading: Boolean = false,
        val recipes: List<Recipe>? = null,
        val error: UiText = UiText.Idle
    )

    sealed interface Navigation {
        data class RecipeDetails(val id: String) : Navigation
        data object Favorite : Navigation
        data object None : Navigation
    }

    sealed interface Event {
        data class Search(val q: String) : Event
        data class NavigateToRecipeDetails(val id: String) : Event
        data object NavigateToFavorite : Event
        data object None : Event
    }
}