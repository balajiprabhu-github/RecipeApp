package com.balajiprabhu.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.balajiprabhu.common.navigation.Destination
import com.balajiprabhu.common.navigation.Feature
import com.balajiprabhu.common.navigation.NavigationSubGraphRoutes

interface SearchFeature : Feature

class SearchFeatureImpl : SearchFeature {

    override fun registerFeature(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.navigation<NavigationSubGraphRoutes.RecipeList>(
            startDestination = Destination.RecipeList) {
            composable<Destination.RecipeList>() {

            }
        }
    }

}
