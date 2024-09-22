package com.balajiprabhu.domain.useCases

import com.balajiprabhu.domain.repository.SearchRepository
import javax.inject.Inject

class GetAllSavedRecipesUseCase @Inject constructor(
    private val searchRepository: SearchRepository) {
    operator fun invoke() = searchRepository.getSavedRecipes()
}