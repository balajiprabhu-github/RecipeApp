package com.balajiprabhu.data.repository

import com.balajiprabhu.data.local.RecipeDao
import com.balajiprabhu.data.mappers.toDomain
import com.balajiprabhu.data.model.RecipeDTO
import com.balajiprabhu.data.model.RecipeDetailsResponse
import com.balajiprabhu.data.model.RecipeResponse
import com.balajiprabhu.data.remote.SearchService
import com.balajiprabhu.domain.model.Recipe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class SearchRepositoryImplTest {

    private val searchService : SearchService = mock()
    private val recipeDao : RecipeDao = mock()

    private lateinit var searchRepositoryImpl: SearchRepositoryImpl

    @Before
    fun setUp() {
        searchRepositoryImpl = SearchRepositoryImpl(searchService, recipeDao)
    }

    @Test
    fun testGetRecipesReturnSuccess() = runTest {
        `when`(searchService.getRecipes("chicken")).thenReturn(
            Response.success(200,getRecipeResponse())
        )
        val response = searchRepositoryImpl.getRecipes("chicken")

        assertEquals(getRecipeResponse().meals?.toDomain(),response.getOrThrow())
    }

    @Test
    fun testGetRecipesReturnsNull() = runTest {
        `when`(searchService.getRecipes("chicken")).thenReturn(
            Response.success(200, RecipeResponse(meals = null))
        )
        val response = searchRepositoryImpl.getRecipes("chicken")

        assertEquals(response.exceptionOrNull()?.message,"error retrieving")
    }

    @Test
    fun testGetRecipesReturnsFailure() = runTest {
        `when`(searchService.getRecipes("chicken")).thenReturn(
            Response.error(404, ResponseBody.create(null,"" )
        ))
        val response = searchRepositoryImpl.getRecipes("chicken")

        assertEquals(response.exceptionOrNull()?.message,"Response.error()")
    }

    @Test
    fun testGetRecipesThrowsException() = runTest {
        `when`(searchService.getRecipes("chicken")).thenThrow(
            RuntimeException("error")
        )

        val response = searchRepositoryImpl.getRecipes("chicken")

        assertEquals(response.exceptionOrNull()?.message,"error")
    }

    @Test
    fun testGetRecipeDetailsReturnSuccess() = runTest {
        `when`(searchService.getRecipeDetails("123")).thenReturn(
            Response.success(200,getRecipeDetails())
        )

        val response = searchRepositoryImpl.getRecipeDetails("123")
        assertEquals(getRecipeDetails().meals?.first()?.toDomain(),response.getOrThrow())
    }

    @Test
    fun testGetRecipesReturnDetailsEmptyList() = runTest {
        `when`(searchService.getRecipes("chicken")).thenReturn(
            Response.success(200, RecipeResponse(meals = emptyList()))
        )
        val response = searchRepositoryImpl.getRecipes("chicken")
        assertEquals(emptyList<Recipe>(),response.getOrThrow())
    }

    @Test
    fun testGetRecipeDetailsReturnsNull() = runTest {
        `when`(searchService.getRecipeDetails("123")).thenReturn(
            Response.success(200, RecipeDetailsResponse(meals = null))
        )

        val response = searchRepositoryImpl.getRecipeDetails("123")
        assertEquals(response.exceptionOrNull()?.message,"error retrieving")
    }

    @Test
    fun testGetRecipeDetailsReturnsFailure() = runTest {
        `when`(searchService.getRecipeDetails("123")).thenReturn(
            Response.error(
                404, ResponseBody.create(null, "")
            )
        )

        val response = searchRepositoryImpl.getRecipeDetails("123")
        assertEquals(response.exceptionOrNull()?.message,"Response.error()")
    }

    @Test
    fun testGetRecipeDetailsThrowsException() = runTest {
        `when`(searchService.getRecipeDetails("123")).thenThrow(
            RuntimeException("error")
        )

        val response = searchRepositoryImpl.getRecipeDetails("123")
        assertEquals(response.exceptionOrNull()?.message,"error")
    }

    @Test
    fun testInsertRecipe() = runTest {
        val searchRepositoryImpl = SearchRepositoryImpl(searchService, FakeRecipeDao())
        val recipe = getRecipeResponse().meals?.toDomain()?.first()
        searchRepositoryImpl.insertRecipe(recipe!!)
        assertEquals(recipe,searchRepositoryImpl.getSavedRecipes().first().first())
    }

    @Test
    fun testDeleteRecipe() = runTest {
        val searchRepositoryImpl = SearchRepositoryImpl(searchService, FakeRecipeDao())
        val recipe = getRecipeResponse().meals?.toDomain()?.first()
        searchRepositoryImpl.insertRecipe(recipe!!)
        assertEquals(recipe,searchRepositoryImpl.getSavedRecipes().first().first())
        searchRepositoryImpl.deleteRecipe(recipe)
        assertEquals(emptyList<Recipe>(),searchRepositoryImpl.getSavedRecipes().first())
    }

    @Test
    fun testGetSavedRecipes() = runTest {
        val searchRepositoryImpl = SearchRepositoryImpl(searchService, FakeRecipeDao())
        val recipe = getRecipeResponse().meals?.toDomain()?.first()
        searchRepositoryImpl.insertRecipe(recipe!!)
        assertEquals(recipe,searchRepositoryImpl.getSavedRecipes().first().first())
    }


    private fun getRecipeResponse(): RecipeResponse {
        return RecipeResponse(
            meals = listOf(
                RecipeDTO(
                    dateModified = "",
                    idMeal = "123",
                    strArea = "Indian",
                    strCategory = "Main Course",
                    strCreativeCommonsConfirmed = "",
                    strDrinkAlternate = "",
                    strImageSource = "",
                    strIngredient1 = "Ingredient 1",
                    strIngredient2 = "Ingredient 2",
                    strIngredient3 = "Ingredient 3",
                    strIngredient4 = "Ingredient 4",
                    strIngredient5 = "Ingredient 5",
                    strIngredient6 = "Ingredient 6",
                    strIngredient7 = "Ingredient 7",
                    strIngredient8 = "Ingredient 8",
                    strIngredient9 = "Ingredient 9",
                    strIngredient10 = "Ingredient 10",
                    strIngredient11 = "Ingredient 11",
                    strIngredient12 = "Ingredient 12",
                    strIngredient13 = "Ingredient 13",
                    strIngredient14 = "Ingredient 14",
                    strIngredient15 = "Ingredient 15",
                    strIngredient16 = "",
                    strIngredient17 = "",
                    strIngredient18 = "",
                    strIngredient19 = "",
                    strIngredient20 = "",
                    strInstructions = "Instructions",
                    strMeal = "Meal",
                    strMealThumb = "Meal Thumb",
                    strMeasure1 = "Measure 1",
                    strMeasure2 = "Measure 2",
                    strMeasure3 = "Measure 3",
                    strMeasure4 = "Measure 4",
                    strMeasure5 = "Measure 5",
                    strMeasure6 = "Measure 6",
                    strMeasure7 = "Measure 7",
                    strMeasure8 = "Measure 8",
                    strMeasure9 = "Measure 9",
                    strMeasure10 = "Measure 10",
                    strMeasure11 = "Measure 11",
                    strMeasure12 = "Measure 12",
                    strMeasure13 = "Measure 13",
                    strMeasure14 = "Measure 14",
                    strMeasure15 = "Measure 15",
                    strMeasure16 = "",
                    strMeasure17 = "",
                    strMeasure18 = "",
                    strMeasure19 = "",
                    strMeasure20 = "",
                    strSource = "",
                    strTags = "",
                    strYoutube = ""
                ),
            )
        )
    }

    private fun getRecipeDetails(): RecipeDetailsResponse {
        return RecipeDetailsResponse(
            meals = listOf(
                getRecipeResponse().meals?.first()!!
            )
        )
    }
}