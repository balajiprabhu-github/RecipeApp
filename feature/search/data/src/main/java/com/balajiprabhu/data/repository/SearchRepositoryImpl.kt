package com.balajiprabhu.data.repository

import com.balajiprabhu.data.local.RecipeDao
import com.balajiprabhu.data.mappers.toDomain
import com.balajiprabhu.data.remote.SearchService
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.model.RecipeDetails
import com.balajiprabhu.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val searchService: SearchService,
    private val recipeDao: RecipeDao
) : SearchRepository {

    override suspend fun getRecipes(s: String): Result<List<Recipe>> {
        return try {
            val response = searchService.getRecipes(s)
            if (response.isSuccessful) {
                response.body()?.meals?.let {
                    Result.success(it.toDomain())
                } ?: run { Result.failure(Exception("error retrieving")) }
            } else {
                Result.failure(Throwable(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipeDetails(id: String): Result<RecipeDetails> {
        return try {
            val response = searchService.getRecipeDetails(id)
            if (response.isSuccessful) {
                response.body()?.meals?.let {
                    if (it.isNotEmpty()) {
                        Result.success(it.first().toDomain())
                    } else {
                        Result.failure(Exception("No recipe found"))
                    }
                } ?: run { Result.failure(Exception("error retrieving")) }
            } else {
                Result.failure(Throwable(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun insertRecipe(recipe: Recipe) {
       recipeDao.insertRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    override fun getSavedRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }
}