package com.balajiprabhu.search.screens.recipeList

import com.balajiprabhu.common.utils.NetworkResult
import com.balajiprabhu.common.utils.UiText
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.model.RecipeDetails
import com.balajiprabhu.domain.useCases.GetAllRecipesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class RecipeListViewModelTest {

    @get:Rule(order = 1)
    val mainDispatcherRule: MainDispatcherRule = MainDispatcherRule()

    private val getAllRecipesUseCase: GetAllRecipesUseCase = mock()

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun testGetRecipeListSuccess() = runTest {
        `when`(getAllRecipesUseCase.invoke("Chicken")).thenReturn(flowOf(NetworkResult.Success(data = getRecipeResponse())))
        val viewModel = RecipeListViewModel(getAllRecipesUseCase)
        viewModel.onEvent(RecipeList.Event.Search("Chicken"))
        assertEquals(getRecipeResponse(), viewModel.uiState.value.recipes)
    }

    @Test
    fun testGetRecipeListError() = runTest {
        `when`(getAllRecipesUseCase.invoke("Chicken")).thenReturn(flowOf(NetworkResult.Error(error = "error")))
        val viewModel = RecipeListViewModel(getAllRecipesUseCase)
        viewModel.onEvent(RecipeList.Event.Search("Chicken"))
        assertEquals(UiText.RemoteString("Something went wrong"), viewModel.uiState.value.error)
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
            ), Recipe(
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

class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}
