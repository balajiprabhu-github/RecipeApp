package com.balajiprabhu.receipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.balajiprabhu.receipes.navigation.DefaultNavigator
import com.balajiprabhu.receipes.navigation.RecipeNavigation
import com.balajiprabhu.receipes.ui.theme.ReceipesTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var defaultNavigator: DefaultNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReceipesTheme {
               Surface(modifier = Modifier
                   .padding(
                       WindowInsets.systemBars.only(
                           WindowInsetsSides.Top + WindowInsetsSides.Bottom
                       ).asPaddingValues()
                   )) {
                   RecipeNavigation(
                       defaultNavigator = defaultNavigator
                   )
               }
            }
        }
    }
}