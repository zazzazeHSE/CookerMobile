@file:OptIn(FlowPreview::class, ObsoleteCoroutinesApi::class)

package on.the.stove.presentation.recipeDetails

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import on.the.stove.core.Resource
import on.the.stove.core.toResource
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Ingredient
import on.the.stove.dto.Recipe
import on.the.stove.presentation.BaseStore
import on.the.stove.services.network.RecipesApi
import org.koin.core.component.inject


class RecipeDetailsStore(private val recipeId: String) :
    BaseStore<RecipeDetailsState, RecipeDetailsAction, RecipeDetailsEffect>() {

    private val recipesApi: RecipesApi by inject()
    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override val stateFlow: MutableStateFlow<RecipeDetailsState> =
        MutableStateFlow(RecipeDetailsState(recipeId))
    override val sideEffectsFlow: MutableSharedFlow<RecipeDetailsEffect> = MutableSharedFlow()

    override suspend fun reduce(action: RecipeDetailsAction, initialState: RecipeDetailsState) {
        when (action) {
            is RecipeDetailsAction.Init -> {
                observeFavouritesRecipes()
                observeIngredientsInCart()
                loadRecipeDetails(stateFlow.value.recipeId)
            }
            is RecipeDetailsAction.Like -> {
                val recipeDetails = requireNotNull(stateFlow.value.recipeResource.value)

                if (recipeDetails.isLiked) {
                    appDatabaseRepository.removeFavouriteRecipe(recipeDetails.id)
                } else {
                    appDatabaseRepository.addFavouriteRecipe(recipeDetails.recipe)
                }
            }
            is RecipeDetailsAction.SelectIngredient -> {
                val ingredient = action.ingredient

                if (ingredient.inCart) {
                    appDatabaseRepository.removeIngredient(ingredient)
                } else {
                    appDatabaseRepository.addIngredient(ingredient)
                }
            }
        }
    }

    private suspend fun loadRecipeDetails(id: String) {
        recipesApi.getRecipeDetails(id).fold(
            onSuccess = { result ->
                val liked = appDatabaseRepository.getAllFavouritesRecipes().map(Recipe::id)
                    .contains(result.id)
                val ingredientsInCart =
                    appDatabaseRepository.getAllIngredients().map(Ingredient::id)

                updateState { state ->
                    state.copy(
                        recipeResource = result.copy(
                            isLiked = liked,
                            ingredients = result.ingredients.map { ingredient ->
                                ingredient.copy(
                                    inCart = ingredientsInCart.contains(ingredient.id)
                                )
                            }
                        ).toResource()
                    )
                }
            },
            onFailure = { error ->
                updateState { state ->
                    state.copy(recipeResource = Resource.Error(error))
                }
            }
        )
    }

    private fun observeFavouritesRecipes() = scope.launch(ioDispatcher) {
        appDatabaseRepository.observeAllFavouritesRecipes()
            .collect { favouriteRecipes ->
                val favouriteIds = favouriteRecipes.map(Recipe::id)
                updateState { state ->
                    state.recipeResource.value?.let { value ->
                        state.copy(
                            recipeResource = value.copy(
                                isLiked = favouriteIds.contains(value.id)
                            ).toResource()
                        )
                    } ?: state
                }
            }
    }

    private fun observeIngredientsInCart() = scope.launch(ioDispatcher) {
        appDatabaseRepository.observeAllIngredients()
            .collect { ingredients ->
                val ingredientsInCart = ingredients.map(Ingredient::id)
                updateState { state ->
                    state.recipeResource.value?.let { value ->
                        state.copy(
                            recipeResource = value.copy(
                                ingredients = value.ingredients.map { ingredient ->
                                    print(ingredientsInCart.contains(ingredient.id))
                                    ingredient.copy(inCart = ingredientsInCart.contains(ingredient.id))
                                }
                            ).toResource()
                        )
                    } ?: state
                }
            }
    }
}