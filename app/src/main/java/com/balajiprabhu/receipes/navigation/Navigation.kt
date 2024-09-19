package com.balajiprabhu.receipes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.balajiprabhu.common.navigation.NavigationSubGraphRoutes

@Composable
fun RecipeNavigation(
    defaultNavigator: DefaultNavigator
) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = NavigationSubGraphRoutes.RecipeList
    ) {
        defaultNavigator.searchFeature.registerFeature(
            navGraphBuilder = this,
            navController = navHostController
        )
    }
}