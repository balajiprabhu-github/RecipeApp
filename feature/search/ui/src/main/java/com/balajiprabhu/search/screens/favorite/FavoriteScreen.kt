package com.balajiprabhu.search.screens.favorite

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.balajiprabhu.common.navigation.Destination
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onRecipeClick: (String) -> Unit,
    viewModel: FavoriteViewModel,
    navHostController: NavHostController
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { navigation ->
                when (navigation) {

                    FavoriteWrapper.Navigation.PopBackStack -> {
                        navHostController.popBackStack()
                    }

                    is FavoriteWrapper.Navigation.RecipeDetails -> {
                        navHostController.navigate(Destination.RecipeDetails(id = navigation.id))
                    }

                }
            }
    }

    LaunchedEffect(key1 = uiState.value.isLoading) {
        viewModel.onEvent(FavoriteWrapper.Event.FetchAllSavedRecipes)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Favorite Recipes", maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { it ->

        if (uiState.value.isLoading) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.value.isEmpty) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recipes found",
                )
            }
        }

        uiState.value.recipes.let { recipes ->
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (recipes != null) {
                    items(recipes) {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable { onRecipeClick.invoke(it.id) },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            AsyncImage(
                                model = it.mealThumbnail,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        all = 8.dp
                                    )
                            ) {
                                Text(
                                    text = it.meal, style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = it.instructions,
                                    style = MaterialTheme.typography.bodyMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 4
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                            }

                            if (it.tags.isNotEmpty()) {
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                ) {
                                    it.tags.split(",").forEach { tag ->
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .wrapContentSize()
                                                .clip(RoundedCornerShape(12.dp))
                                                .border(
                                                    width = 1.dp,
                                                    color = Color.Red,
                                                    shape = RoundedCornerShape(12.dp)
                                                ), contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = tag,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp, vertical = 4.dp
                                                )
                                            )
                                        }
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}