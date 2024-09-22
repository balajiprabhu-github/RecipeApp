package com.balajiprabhu.common.navigation

import kotlinx.serialization.Serializable

sealed class NavigationSubGraphRoutes {
    @Serializable
    data object RecipeList : NavigationSubGraphRoutes()
}

sealed class Destination {
    @Serializable
    data object RecipeList : Destination()

    @Serializable
    data class RecipeDetails(val id: String) : Destination()

    @Serializable
    data object Favorite : Destination()

    @Serializable
    data class MediaPlayer(val videoId: String) : Destination()
}