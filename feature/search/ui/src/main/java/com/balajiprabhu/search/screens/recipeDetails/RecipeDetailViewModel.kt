package com.balajiprabhu.search.screens.recipeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balajiprabhu.common.utils.NetworkResult
import com.balajiprabhu.common.utils.UiText
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.model.RecipeDetails
import com.balajiprabhu.domain.useCases.DeleteRecipeUseCase
import com.balajiprabhu.domain.useCases.GetRecipeDetailsUseCase
import com.balajiprabhu.domain.useCases.InsertRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailsWrapper.UiState())
    val uiState: StateFlow<RecipeDetailsWrapper.UiState> get() = _uiState.asStateFlow()

    private val _navigation = Channel<RecipeDetailsWrapper.Navigation>()
    val navigation get() = _navigation.receiveAsFlow()

    fun onEvent(event: RecipeDetailsWrapper.Event) {
        when (event) {
            is RecipeDetailsWrapper.Event.FetchRecipeDetails -> {
                getRecipeDetails(event.id)
            }

            is RecipeDetailsWrapper.Event.OnBackClick -> {
                viewModelScope.launch {
                    _navigation.send(RecipeDetailsWrapper.Navigation.PopBackStack)
                }
            }

            is RecipeDetailsWrapper.Event.DeleteRecipe -> {
                deleteRecipeUseCase(event.recipe.toRecipe()).launchIn(viewModelScope)
            }

            is RecipeDetailsWrapper.Event.InsertRecipe -> {
                insertRecipeUseCase(event.recipe.toRecipe()).launchIn(viewModelScope)
            }
        }
    }

    private fun getRecipeDetails(id: String) =
        getRecipeDetailsUseCase.invoke(id)
            .onEach { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.update {
                            RecipeDetailsWrapper.UiState(isLoading = true)
                        }
                    }

                    is NetworkResult.Success -> {
                        _uiState.update {
                            RecipeDetailsWrapper.UiState(recipeDetails = result.data)
                        }
                    }

                    is NetworkResult.Error -> {
                        _uiState.update {
                            RecipeDetailsWrapper.UiState(
                                error = UiText.RemoteString(
                                    result.error ?: "Something went wrong"
                                )
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)

    private fun RecipeDetails.toRecipe() : Recipe {
        return Recipe(
            id = id,
            meal = meal,
            mealThumbnail = mealThumbnail,
            category = category,
            area = area,
            instructions = instructions,
            tags = tags,
            youtube = youtube
        )
    }
}

object RecipeDetailsWrapper {

    data class UiState(
        val isLoading: Boolean = false,
        val recipeDetails: RecipeDetails? = null,
        val error: UiText = UiText.Idle
    )

    sealed interface Navigation {
        data object PopBackStack : Navigation
    }

    sealed interface Event {
        data class FetchRecipeDetails(val id: String) : Event
        data class InsertRecipe(val recipe: RecipeDetails) : Event
        data class DeleteRecipe(val recipe: RecipeDetails) : Event
        data class OnBackClick(val navigation: Navigation) : Event
    }
}