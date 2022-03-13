package on.the.stove.database

import kotlinx.coroutines.flow.Flow
import on.the.stove.dto.Ingredient
import on.the.stove.dto.Recipe

private typealias RecipesList = List<Recipe>
private typealias IngredientsList = List<Ingredient>

internal interface AppDatabaseRepository {

    fun observeAllFavouritesRecipes(): Flow<RecipesList>

    fun getAllFavouritesRecipes(): RecipesList

    fun addFavouriteRecipe(recipe: Recipe)

    fun removeFavouriteRecipe(recipeId: String)

    fun observeAllIngredients(): Flow<IngredientsList>

    fun getAllIngredients(): IngredientsList

    fun addIngredient(ingredient: Ingredient)

    fun removeIngredient(ingredient: Ingredient)
}