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

internal class AppDatabaseManager() : KoinComponent {
    private val databaseDriverFactory: DatabaseDriverFactory by inject()

    private val database = AppDatabase(databaseDriverFactory.createDriver())

    fun getAllFavouritesRecipes() = database.favouriteRecipesTableQueries
        .getAllFavouriteRecipes()
        .asFlow()
        .mapToList()
        .map { it.map { recipeDb ->
            Recipe(
                id = recipeDb.id,
                author = recipeDb.author,
                title = recipeDb.title,
                description = recipeDb.description,
                imageUrl = recipeDb.imageUrl,
                isLiked = true,
            )
        } }
        .flowOn(ioDispatcher)

    fun addOrUpdateFavouriteRecipe(recipe: Recipe) = database.favouriteRecipesTableQueries
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
