package com.balajiprabhu.data.mappers

import com.balajiprabhu.data.model.RecipeDTO
import com.balajiprabhu.domain.model.Recipe
import com.balajiprabhu.domain.model.RecipeDetails

fun List<RecipeDTO>.toDomain(): List<Recipe> = map {
    Recipe(
        id = it.idMeal,
        meal = it.strMeal,
        area = it.strArea,
        mealThumbnail = it.strMealThumb,
        category = it.strCategory,
        tags = it.strTags ?: "",
        youtube = it.strYoutube ?: "",
        instructions = it.strInstructions
    )
}

fun RecipeDTO.toDomain(): RecipeDetails {
    return RecipeDetails(
        id = this.idMeal,
        meal = this.strMeal,
        area = this.strArea,
        mealThumbnail = this.strMealThumb,
        category = this.strCategory,
        tags = this.strTags ?: "",
        youtube = this.strYoutube ?: "",
        instructions = this.strInstructions,
        ingredientsPair = this.getIngredientPairsWithItsMeasure()
    )
}

fun RecipeDTO.getIngredientPairsWithItsMeasure(): List<Pair<String,String>> {
    val list = mutableListOf<Pair<String,String>>()
    list.add(Pair(strIngredient1.getOrEmpty(), strMeasure1.getOrEmpty()))
    list.add(Pair(strIngredient2.getOrEmpty(), strMeasure2.getOrEmpty()))
    list.add(Pair(strIngredient3.getOrEmpty(), strMeasure3.getOrEmpty()))
    list.add(Pair(strIngredient4.getOrEmpty(), strMeasure4.getOrEmpty()))
    list.add(Pair(strIngredient5.getOrEmpty(), strMeasure5.getOrEmpty()))
    list.add(Pair(strIngredient6.getOrEmpty(), strMeasure6.getOrEmpty()))
    list.add(Pair(strIngredient7.getOrEmpty(), strMeasure7.getOrEmpty()))
    list.add(Pair(strIngredient8.getOrEmpty(), strMeasure8.getOrEmpty()))
    list.add(Pair(strIngredient9.getOrEmpty(), strMeasure9.getOrEmpty()))
    list.add(Pair(strIngredient10.getOrEmpty(), strMeasure10.getOrEmpty()))
    list.add(Pair(strIngredient11.getOrEmpty(), strMeasure11.getOrEmpty()))
    list.add(Pair(strIngredient12.getOrEmpty(), strMeasure12.getOrEmpty()))
    list.add(Pair(strIngredient13.getOrEmpty(), strMeasure13.getOrEmpty()))
    list.add(Pair(strIngredient14.getOrEmpty(), strMeasure14.getOrEmpty()))
    list.add(Pair(strIngredient15.getOrEmpty(), strMeasure15.getOrEmpty()))
    return list
}

fun String.getOrEmpty() = if(this.isEmpty()) "" else this.toString()