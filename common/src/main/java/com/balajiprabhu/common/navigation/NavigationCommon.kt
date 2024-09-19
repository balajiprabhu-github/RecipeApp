package com.balajiprabhu.common.navigation

import kotlinx.serialization.Serializable

sealed class NavigationSubGraphRoutes {
    @Serializable
    data object RecipeList : NavigationSubGraphRoutes()
    @Serializable
    data object RecipeDetails : NavigationSubGraphRoutes()
}

sealed class Destination {
    @Serializable
    data object RecipeList : Destination()
    @Serializable
    data object RecipeDetails : Destination()
}