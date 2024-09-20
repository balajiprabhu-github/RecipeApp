package com.balajiprabhu.search.screens.recipeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balajiprabhu.common.utils.NetworkResult
import com.balajiprabhu.common.utils.UiText
import com.balajiprabhu.domain.model.RecipeDetails
import com.balajiprabhu.domain.useCases.GetRecipeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailsWrapper.UiState())
    val uiState: StateFlow<RecipeDetailsWrapper.UiState> get() = _uiState.asStateFlow()

    fun onEvent(event: RecipeDetailsWrapper.Event) {
        when (event) {
            is RecipeDetailsWrapper.Event.FetchRecipeDetails -> {
                getRecipeDetails(event.id)
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
}

object RecipeDetailsWrapper {

    data class UiState(
        val isLoading: Boolean = false,
        val recipeDetails: RecipeDetails? = null,
        val error: UiText = UiText.Idle
    )

    sealed interface Event {
        data class FetchRecipeDetails(val id: String) : Event
    }
}