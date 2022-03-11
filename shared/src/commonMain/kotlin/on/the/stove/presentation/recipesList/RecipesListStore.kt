package on.the.stove.presentation.recipesList

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import on.the.stove.core.Resource
import on.the.stove.core.toResource
import on.the.stove.database.AppDatabaseManager
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
                updateState { state ->
                    state.copy(
                        recipesResource = state.recipesResource.value.orEmpty().map { recipe ->
                            if (recipe.id == action.id) {
                                recipe.copy(isLiked = !recipe.isLiked)
                            } else {
                                recipe
                            }
                        }.toResource()
                    )
                }
                updateRecipeCacheById(id = action.id)
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

    private fun updateRecipeCacheById(id: String) {
        val recipe = stateFlow.value.recipesResource.value!!.first { recipe ->
            recipe.id == id
        }

        if (recipe.isLiked) {
            appDatabaseManager.addOrUpdateFavouriteRecipe(recipe)
        } else {
            appDatabaseManager.removeFavouriteRecipe(recipe.id)
        }
    }

    private suspend inline fun List<Recipe>.mergeCachedAndInvoke(crossinline andThen: suspend (mergedRecipes: List<Recipe>) -> Unit) {
        appDatabaseManager.getAllFavouritesRecipes().collect { dbRecipes ->
            val mergedRecipes = map { recipe ->
                if (dbRecipes.find { it.id == recipe.id } != null) {
                    appDatabaseManager.addOrUpdateFavouriteRecipe(recipe)

                    recipe.copy(isLiked = true)
                } else {
                    recipe
                }
            }

            andThen.invoke(mergedRecipes)
        }
    }
}