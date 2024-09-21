package com.balajiprabhu.search.screens.recipeDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.balajiprabhu.common.utils.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    modifier: Modifier = Modifier, viewModel: RecipeDetailViewModel
) {

    val scrollState = rememberScrollState()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        val recipeDetail = uiState.value.recipeDetails
        if (recipeDetail != null) {
            Text(
                text = recipeDetail.meal,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp))
        }
    }) { innerPadding ->

        if (uiState.value.isLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.value.error !is UiText.Idle) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.value.error.getString(LocalContext.current),
                )
            }
        }

        uiState.value.recipeDetails?.let { model ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                AsyncImage(
                    model = model.mealThumbnail,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = model.instructions, style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    model.ingredientsPair.forEach { pair ->
                        if (pair.first.isNotEmpty().and(pair.first.isNotBlank()) ||
                            pair.second.isNotEmpty().and(pair.second.isNotBlank())) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = getIngredientsImageUrl(pair.first),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(
                                            color = Color.Transparent, shape = CircleShape
                                        )
                                        .clip(CircleShape)
                                )

                                Text(
                                    text = pair.second, style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }


                    Text(
                        text = "Watch YouTube Video",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

fun getIngredientsImageUrl(ingredient: String): String =
    "https://www.themealdb.com/images/ingredients/$ingredient.png"