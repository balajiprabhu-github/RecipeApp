package com.balajiprabhu.receipes.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.balajiprabhu.data.local.RecipeDao
import com.balajiprabhu.domain.model.IngredientsConverter
import com.balajiprabhu.domain.model.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(IngredientsConverter::class)
abstract class ApplicationDataBase : RoomDatabase() {
    companion object {
        fun getInstance(context: Context) = Room.databaseBuilder(
            context, ApplicationDataBase::class.java, "recipe_database"
        ).fallbackToDestructiveMigration().build()
    }

    abstract fun getRecipeDao() : RecipeDao
}