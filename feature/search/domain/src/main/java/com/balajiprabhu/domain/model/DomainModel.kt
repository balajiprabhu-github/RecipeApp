package com.balajiprabhu.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "recipe")
@TypeConverters(IngredientsConverter::class)
data class Recipe(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val area: String,
    val meal: String,
    val mealThumbnail: String,
    val category: String,
    val tags: String,
    val youtube: String,
    val instructions: String
)

data class RecipeDetails(
    val id: String,
    val area: String,
    val meal: String,
    val mealThumbnail: String,
    val category: String,
    val tags: String,
    val youtube: String,
    val instructions: String,
    val ingredientsPair: List<Pair<String, String>>
)