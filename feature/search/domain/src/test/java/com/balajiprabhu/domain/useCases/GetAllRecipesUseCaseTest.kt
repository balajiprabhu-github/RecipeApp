package com.balajiprabhu.domain.useCases

import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.model.RecipeDetails
import com.balajiprabhu.domain.repository.SearchRepository
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetAllRecipesUseCaseTest {

    private val searchRepository: SearchRepository = mock()

    private val getAllRecipesUseCase = GetAllRecipesUseCase(searchRepository)

    @Test
    fun `invoke should return a list of recipes`() = runTest {
        `when`(searchRepository.getRecipes("chicken"))
            .thenReturn(Result.success(getRecipeResponse()))
        val result = getAllRecipesUseCase.invoke("chicken")
        assertEquals(getRecipeResponse(), result.last().data)
    }

    @Test
    fun `invoke should return an error message`() = runTest {
        `when`(searchRepository.getRecipes("chicken"))
            .thenReturn(Result.failure(Exception("error")))
        val result = getAllRecipesUseCase.invoke("chicken")
        assertEquals("error",result.last().error)
    }

    @Test
    fun `invoke throws exception`() = runTest {
        `when`(searchRepository.getRecipes("chicken"))
            .thenThrow(RuntimeException("error"))
        val result = getAllRecipesUseCase.invoke("chicken")
        assertEquals("error",result.last().error)
    }

    private fun getRecipeResponse(): List<Recipe> {
        return listOf(
            Recipe(
                id = "idMeal",
                area = "India",
                category = "category",
                youtube = "strYoutube",
                tags = "tag1,tag2",
                meal = "Chicken",
                mealThumbnail = "strMealThumb",
                instructions = "12"
            ),
            Recipe(
                id = "idMeal",
                area = "India",
                category = "category",
                youtube = "strYoutube",
                tags = "tag1,tag2",
                meal = "Chicken",
                mealThumbnail = "strMealThumb",
                instructions = "123",
            )
        )

    }

    private fun getRecipeDetails(): RecipeDetails {
        return RecipeDetails(
            id = "idMeal",
            area = "India",
            category = "category",
            youtube = "strYoutube",
            tags = "tag1,tag2",
            meal = "Chicken",
            mealThumbnail = "strMealThumb",
            instructions = "strInstructions",
            ingredientsPair = listOf(Pair("Ingredients", "Measure"))
        )
    }
}