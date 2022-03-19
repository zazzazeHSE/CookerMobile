package on.the.stove.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import on.the.stove.dto.Ingredient
import on.the.stove.dto.Note
import on.the.stove.dto.Recipe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tables.AppDatabase
import tables.FavouriteRecipe
import tables.IngredientInCart

internal class AppDatabaseRepositoryImpl : KoinComponent, AppDatabaseRepository {
    private val databaseDriverFactory: DatabaseDriverFactory by inject()

    private val database: AppDatabase by lazy {
        AppDatabase(databaseDriverFactory.createDriver())
    }

    override fun observeAllFavouritesRecipes() = database.favouriteRecipesTableQueries
        .getAllFavouriteRecipes()
        .asFlow()
        .mapToList()
        .map { favoriteRecipes -> favoriteRecipes.map { it.toRecipe() } }

    override fun getAllFavouritesRecipes() = database.favouriteRecipesTableQueries
        .getAllFavouriteRecipes()
        .executeAsList()
        .map { it.toRecipe() }

    override fun addFavouriteRecipe(recipe: Recipe) = database.favouriteRecipesTableQueries
        .insertOrUpdateFavoriteRecipes(
            id = recipe.id,
            description = recipe.description,
            title = recipe.title,
            imageUrl = recipe.imageUrl,
        )

    override fun removeFavouriteRecipe(recipeId: String) = database.favouriteRecipesTableQueries
        .deleteFavouriteRecipe(id = recipeId)

    private fun FavouriteRecipe.toRecipe() = Recipe(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        isLiked = true,
    )

    private fun IngredientInCart.toIngredient() = Ingredient(
        name = name,
        value = value_
    )

    override fun observeAllIngredients() = database.ingredientsTableQueries
        .getAllIngredientsInCart()
        .asFlow()
        .mapToList()
        .map { ingredientsInCart -> ingredientsInCart.map { it.toIngredient() } }

    override fun getAllIngredients() = database.ingredientsTableQueries
        .getAllIngredientsInCart()
        .executeAsList()
        .map { it.toIngredient() }

    override fun addIngredient(ingredient: Ingredient) = database
        .ingredientsTableQueries
        .insertOrUpdateIngredientsInCart(
            id = ingredient.id,
            name = ingredient.name,
            value_ = ingredient.value
        )

    override fun removeIngredient(ingredient: Ingredient) = database.ingredientsTableQueries
        .deleteIngredientInCart(ingredient.id)

    override fun getNote(recipeId: String) = runCatching {
        database.notesTableQueries.getNote(id = recipeId).executeAsOne().let { note ->
            Note(
                id = note.id,
                content = note.content,
            )
        }
    }.getOrNull()

    override fun updateNode(note: Note) {
        database.notesTableQueries.insertNote(
            id = note.id,
            content = note.content,
        )
    }
}