package com.balajiprabhu.domain.model

import androidx.room.TypeConverter

class IngredientsConverter {
    @TypeConverter
    fun fromIngredientsList(ingredients: List<Pair<String, String>>): String {
        return ingredients.joinToString(",") { "${it.first}-${it.second}" }
    }

    @TypeConverter
    fun toIngredientsList(data: String): List<Pair<String, String>> {
        return data.split(",").map {
            val parts = it.split("-")
            Pair(parts[0], parts[1])
        }
    }
}