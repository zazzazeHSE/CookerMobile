package on.the.stove.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Recipe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tables.AppDatabase
import tables.FavouriteRecipe

internal class AppDatabaseManager : KoinComponent {
    private val databaseDriverFactory: DatabaseDriverFactory by inject()

    private val database : AppDatabase by inject()

    fun observeAllFavouritesRecipes() = database.favouriteRecipesTableQueries
        .getAllFavouriteRecipes()
        .asFlow()
        .flowOn(ioDispatcher)
        .mapToList()
        .map { favoriteRecipes -> favoriteRecipes.map { it.toRecipe() } }

    fun getAllFavouritesRecipes() = database.favouriteRecipesTableQueries
        .getAllFavouriteRecipes()
        .executeAsList()
        .map { it.toRecipe() }

    fun addFavouriteRecipe(recipe: Recipe) = database.favouriteRecipesTableQueries
        .insertOrUpdateFavoriteRecipes(
            id = recipe.id,
            author = recipe.author,
            description = recipe.description,
            title = recipe.title,
            imageUrl = recipe.imageUrl,
        )

    fun removeFavouriteRecipe(recipeId: String) = database.favouriteRecipesTableQueries
        .deleteFavouriteRecipe(id = recipeId)
}

private fun FavouriteRecipe.toRecipe() = Recipe(
    id = id,
    author = author,
    title = title,
    description = description,
    imageUrl = imageUrl,
    isLiked = true,
)