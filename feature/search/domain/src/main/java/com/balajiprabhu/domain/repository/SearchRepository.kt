package com.balajiprabhu.domain.repository

import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.model.RecipeDetails

interface SearchRepository {
    suspend fun getRecipes(s:String) : Result<List<Recipe>>
    suspend fun getRecipeDetails(id: String) : Result<RecipeDetails>
}