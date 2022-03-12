package on.the.stove.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Recipe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tables.AppDatabase
import tables.FavouriteRecipe

internal class AppDatabaseRepositoryImpl : KoinComponent, AppDatabaseRepository {
    private val database: AppDatabase by inject()

    override fun observeAllFavouritesRecipes() = database.favouriteRecipesTableQueries
        .getAllFavouriteRecipes()
        .asFlow()
        .flowOn(ioDispatcher)
        .mapToList()
        .map { favoriteRecipes -> favoriteRecipes.map { it.toRecipe() } }

    override fun getAllFavouritesRecipes() = database.favouriteRecipesTableQueries
        .getAllFavouriteRecipes()
        .executeAsList()
        .map { it.toRecipe() }

    override fun addFavouriteRecipe(recipe: Recipe) = database.favouriteRecipesTableQueries
        .insertOrUpdateFavoriteRecipes(
            id = recipe.id,
            author = recipe.author,
            description = recipe.description,
            title = recipe.title,
            imageUrl = recipe.imageUrl,
        )

    override fun removeFavouriteRecipe(recipeId: String) = database.favouriteRecipesTableQueries
        .deleteFavouriteRecipe(id = recipeId)

    private fun FavouriteRecipe.toRecipe() = Recipe(
        id = id,
        author = author,
        title = title,
        description = description,
        imageUrl = imageUrl,
        isLiked = true,
    )
}