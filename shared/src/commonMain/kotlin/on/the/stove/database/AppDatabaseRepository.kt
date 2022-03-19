package on.the.stove.database

import kotlinx.coroutines.flow.Flow
import on.the.stove.dto.Ingredient
import on.the.stove.dto.Note
import on.the.stove.dto.Recipe

private typealias RecipesList = List<Recipe>
private typealias IngredientsList = List<Ingredient>

internal interface AppDatabaseRepository {

    // #region Favourites recipes
    fun observeAllFavouritesRecipes(): Flow<RecipesList>

    fun getAllFavouritesRecipes(): RecipesList

    fun addFavouriteRecipe(recipe: Recipe)

    fun removeFavouriteRecipe(recipeId: String)
    // #endregion Favourites recipes

    // #region Ingredients
    fun observeAllIngredients(): Flow<IngredientsList>

    fun getAllIngredients(): IngredientsList

    fun addIngredient(ingredient: Ingredient)

    fun removeIngredient(ingredient: Ingredient)
    // #endregion Ingredients

    fun getNote(recipeId: String): Note?

    fun updateNode(note: Note)
}