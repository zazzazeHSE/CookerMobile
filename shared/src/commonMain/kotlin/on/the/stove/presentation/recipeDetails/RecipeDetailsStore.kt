package on.the.stove.presentation.recipeDetails

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import on.the.stove.core.Resource
import on.the.stove.core.toResource
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Recipe
import on.the.stove.presentation.BaseStore
import on.the.stove.services.network.RecipesApi
import org.koin.core.component.inject

@OptIn(ObsoleteCoroutinesApi::class)
class RecipeDetailsStore :
    BaseStore<RecipeDetailsState, RecipeDetailsAction, RecipeDetailsEffect>() {

    private val recipesApi: RecipesApi by inject()
    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override val stateFlow: MutableStateFlow<RecipeDetailsState> =
        MutableStateFlow(RecipeDetailsState())
    override val sideEffectsFlow: MutableSharedFlow<RecipeDetailsEffect> = MutableSharedFlow()

    override suspend fun reduce(action: RecipeDetailsAction, initialState: RecipeDetailsState) {
        when (action) {
            is RecipeDetailsAction.Init -> {
                observeFavouritesRecipes()
                loadRecipeDetails(action.id)
                observeFavouritesRecipes()
            }
            is RecipeDetailsAction.Like -> {
                val recipeDetails = requireNotNull(stateFlow.value.recipeResource.value)

                if (recipeDetails.isLiked) {
                    appDatabaseRepository.removeFavouriteRecipe(recipeDetails.id)
                } else {
                    appDatabaseRepository.addFavouriteRecipe(recipeDetails.recipe)
                }
            }
        }
    }

    private suspend fun loadRecipeDetails(id: String) {
        recipesApi.getRecipeDetails(id).fold(
            onSuccess = { result ->
                val liked = appDatabaseRepository.getAllFavouritesRecipes().map(Recipe::id).contains(result.id)
                updateState { state ->
                    state.copy(
                        recipeResource = result.copy(isLiked = liked).toResource()
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
                    }?:state
                }
            }
    }
}