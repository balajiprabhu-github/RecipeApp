package com.balajiprabhu.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface Feature {
    fun registerFeature(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    )
}