package com.balajiprabhu.search.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.balajiprabhu.common.navigation.Destination
import com.balajiprabhu.common.navigation.Feature
import com.balajiprabhu.common.navigation.NavigationSubGraphRoutes
import com.balajiprabhu.search.screens.recipeDetails.RecipeDetailViewModel
import com.balajiprabhu.search.screens.recipeDetails.RecipeDetailsScreen
import com.balajiprabhu.search.screens.recipeDetails.RecipeDetailsWrapper
import com.balajiprabhu.search.screens.recipeList.RecipeList
import com.balajiprabhu.search.screens.recipeList.RecipeListScreen
import com.balajiprabhu.search.screens.recipeList.RecipeListViewModel

interface SearchFeature : Feature

class SearchFeatureImpl : SearchFeature {

    override fun registerFeature(
        navGraphBuilder: NavGraphBuilder, navController: NavHostController
    ) {
        navGraphBuilder.navigation<NavigationSubGraphRoutes.RecipeList>(
            startDestination = Destination.RecipeList
        ) {
            composable<Destination.RecipeList> {
                val viewModel = hiltViewModel<RecipeListViewModel>()
                RecipeListScreen(
                    viewModel = viewModel, onRecipeClick = {
                        viewModel.onEvent(RecipeList.Event.NavigateToRecipeDetails(it))
                    }, navHostController = navController
                )
            }

            composable<Destination.RecipeDetails> { entry ->
                val recipeDetails = entry.toRoute<Destination.RecipeDetails>()
                val viewModel = hiltViewModel<RecipeDetailViewModel>()
                LaunchedEffect(key1 = recipeDetails.id) {
                    recipeDetails.id.let {
                        viewModel.onEvent(RecipeDetailsWrapper.Event.FetchRecipeDetails(it))
                    }
                }
                RecipeDetailsScreen(viewModel = viewModel)
            }
        }
    }
}
