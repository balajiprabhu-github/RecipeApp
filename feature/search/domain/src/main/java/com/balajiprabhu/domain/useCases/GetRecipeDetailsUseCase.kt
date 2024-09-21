package com.balajiprabhu.domain.useCases

import com.balajiprabhu.common.utils.NetworkResult
import com.balajiprabhu.domain.model.RecipeDetails
import com.balajiprabhu.domain.repository.SearchRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    operator fun invoke(id: String) = flow<NetworkResult<RecipeDetails>> {
        emit(NetworkResult.Loading())
        val response = searchRepository.getRecipeDetails(id)
        if (response.isSuccess) {
            emit(NetworkResult.Success(data = response.getOrThrow()))
        } else {
            emit(NetworkResult.Error(error = response.exceptionOrNull()?.message))
        }
    }
}