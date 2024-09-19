package com.balajiprabhu.domain.useCases

import com.balajiprabhu.common.utils.NetworkResult
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllRecipesUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    operator fun invoke(q: String) = flow<NetworkResult<List<Recipe>>> {
        emit(NetworkResult.Loading())
        val response = searchRepository.getRecipes(q)
        if(response.isSuccess) {
            emit(NetworkResult.Success(data = response.getOrThrow()))
        } else {
            emit(NetworkResult.Error(error = response.exceptionOrNull()?.message))
        }
    }.catch {
        emit(NetworkResult.Error(error = it.message))
    }.flowOn(Dispatchers.IO)
}