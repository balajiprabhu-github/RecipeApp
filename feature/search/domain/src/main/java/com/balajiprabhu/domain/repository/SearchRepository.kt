package com.balajiprabhu.domain.repository

import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.model.RecipeDetails
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getRecipes(s:String) : Result<List<Recipe>>
    suspend fun getRecipeDetails(id: String) : Result<RecipeDetails>
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    fun getSavedRecipes() : Flow<List<Recipe>>
}