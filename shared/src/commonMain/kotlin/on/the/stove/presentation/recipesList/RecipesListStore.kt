package on.the.stove.presentation.recipesList

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import on.the.stove.core.Resource
import on.the.stove.core.toResource
import on.the.stove.database.AppDatabaseManager
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Recipe
import on.the.stove.presentation.BaseStore
import on.the.stove.services.network.RecipeApiImpl
import on.the.stove.services.network.RecipesApi

@OptIn(ObsoleteCoroutinesApi::class)
class RecipesListStore :
    BaseStore<RecipesListState, RecipesListAction, RecipesListEffect>() {

    // TODO: Use DI in future
    private val recipesApi: RecipesApi = RecipeApiImpl()
    private val appDatabaseManager = AppDatabaseManager()

    override val stateFlow: MutableStateFlow<RecipesListState> =
        MutableStateFlow(RecipesListState())
    override val sideEffectsFlow: MutableSharedFlow<RecipesListEffect> = MutableSharedFlow()

    override suspend fun reduce(action: RecipesListAction, initialState: RecipesListState) {
        when (action) {
            is RecipesListAction.Init -> {
                scope.launch(ioDispatcher) {
                    appDatabaseManager.observeAllFavouritesRecipes().collect { favouriteRecipes ->
                        val favouriteIds = favouriteRecipes.map(Recipe::id)

                        updateState { state ->
                            state.copy(
                                recipesResource = state.recipesResource.value?.map { recipe ->
                                    recipe.copy(
                                        isLiked = favouriteIds.contains(recipe.id)
                                    )
                                }?.toResource() ?: state.recipesResource
                            )
                        }
                    }
                }

                updateState { state ->
                    state.copy(
                        page = 1,
                        recipesResource = Resource.Loading,
                    )
                }

                loadRecipes(state = stateFlow.value)
            }
            is RecipesListAction.LoadNextPage -> {
                updateState { state ->
                    state.copy(
                        page = state.page + 1,
                        isPaginationRecipesLoading = true,
                    )
                }

                loadNextPage(state = stateFlow.value)
            }
            is RecipesListAction.ChangeCategory -> {
                updateState { state ->
                    state.copy(
                        category = action.category
                    )
                }

                loadRecipes(state = stateFlow.value)
            }
            is RecipesListAction.Like -> {
                val recipe = stateFlow.value.recipesResource.value!!.first { recipe ->
                    recipe.id == action.id
                }

                if (recipe.isLiked) {
                    appDatabaseManager.removeFavouriteRecipe(recipe.id)
                } else {
                    appDatabaseManager.addFavouriteRecipe(recipe)
                }
            }
        }
    }

    private suspend fun loadRecipes(state: RecipesListState) {
        recipesApi.getRecipesList(
            page = state.page,
            category = state.category,
        ).fold(
            onSuccess = { recipes ->
                recipes.mergeCachedAndInvoke { mergedRecipes ->
                    updateState { state ->
                        state.copy(
                            recipesResource = Resource.Data(mergedRecipes)
                        )
                    }
                }
            },
            onFailure = { error ->
                updateState { state ->
                    state.copy(
                        recipesResource = Resource.Error(error)
                    )
                }
            }
        )
    }

    private suspend fun loadNextPage(state: RecipesListState) {
        recipesApi.getRecipesList(
            page = state.page,
            category = state.category,
        ).fold(
            onSuccess = { recipes ->
                recipes.mergeCachedAndInvoke { mergedRecipes ->
                    updateState { state ->
                        state.copy(
                            recipesResource = Resource.Data(
                                (state.recipesResource.value ?: emptyList()) + mergedRecipes
                            ),
                            isPaginationRecipesLoading = false,
                        )
                    }
                }
            },
            onFailure = {
                updateState { state ->
                    state.copy(
                        isPaginationRecipesLoading = false,
                    )
                }
            }
        )
    }

    private suspend inline fun List<Recipe>.mergeCachedAndInvoke(crossinline andThen: suspend (mergedRecipes: List<Recipe>) -> Unit) {
        val dbRecipes = appDatabaseManager.getAllFavouritesRecipes()

        val mergedRecipes = map { recipe ->
            if (dbRecipes.find { it.id == recipe.id } != null) {
                recipe.copy(isLiked = true)
            } else {
                recipe
            }
        }

        andThen.invoke(mergedRecipes)
    }
}