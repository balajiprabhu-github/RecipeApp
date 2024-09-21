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
import com.balajiprabhu.search.screens.favorite.FavoriteScreen
import com.balajiprabhu.search.screens.favorite.FavoriteViewModel
import com.balajiprabhu.search.screens.favorite.FavoriteWrapper
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
                RecipeDetailsScreen(viewModel = viewModel, onBackClick = {
                    viewModel.onEvent(
                        RecipeDetailsWrapper.Event.OnBackClick(
                            RecipeDetailsWrapper.Navigation.PopBackStack
                        )
                    )
                }, onDeleteClick = {
                    viewModel.onEvent(RecipeDetailsWrapper.Event.DeleteRecipe(it))
                }, onFavClick = {
                    viewModel.onEvent(RecipeDetailsWrapper.Event.InsertRecipe(it))
                }, navHostController = navController
                )
            }

            composable<Destination.Favorite> {
                val viewModel = hiltViewModel<FavoriteViewModel>()

                FavoriteScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        viewModel.onEvent(FavoriteWrapper.Event.OnBackClick)
                    },
                    onRecipeClick = {
                        viewModel.onEvent(FavoriteWrapper.Event.NavigateRecipeDetail(it))
                    },
                    navHostController = navController
                )
            }
        }
    }
}
