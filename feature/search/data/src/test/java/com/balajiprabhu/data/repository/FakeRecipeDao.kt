package com.balajiprabhu.data.repository

import com.balajiprabhu.data.local.RecipeDao
import com.balajiprabhu.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRecipeDao : RecipeDao {
    private val recipes = mutableListOf<Recipe>()

    override suspend fun insertRecipe(recipe: Recipe) {
        recipes.add(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
       recipes.remove(recipe)
    }

    override fun getAllRecipes(): Flow<List<Recipe>> {
        return flowOf(recipes)
    }

    override suspend fun updateRecipe(recipe: Recipe) {}
}