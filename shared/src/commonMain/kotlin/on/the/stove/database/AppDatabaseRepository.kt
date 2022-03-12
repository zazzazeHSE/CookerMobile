package on.the.stove.database

import kotlinx.coroutines.flow.Flow
import on.the.stove.dto.Recipe

private typealias RecipesList = List<Recipe>

internal interface AppDatabaseRepository {

    fun observeAllFavouritesRecipes(): Flow<RecipesList>

    fun getAllFavouritesRecipes(): RecipesList

    fun addFavouriteRecipe(recipe: Recipe)

    fun removeFavouriteRecipe(recipeId: String)
}