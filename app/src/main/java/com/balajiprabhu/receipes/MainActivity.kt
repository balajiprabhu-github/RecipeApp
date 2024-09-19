package com.balajiprabhu.receipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeContentPadding
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
               Surface(modifier = Modifier.safeContentPadding()) {
                   RecipeNavigation(
                       defaultNavigator = defaultNavigator
                   )
               }
            }
        }
    }
}