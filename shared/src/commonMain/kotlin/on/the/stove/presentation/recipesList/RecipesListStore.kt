package on.the.stove.presentation.recipesList

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import on.the.stove.core.Resource
import on.the.stove.core.toResource
import on.the.stove.presentation.BaseStore
import on.the.stove.services.network.RecipeApiImpl
import on.the.stove.services.network.RecipesApi

@OptIn(ObsoleteCoroutinesApi::class)
class RecipesListStore :
    BaseStore<RecipesListState, RecipesListAction, RecipesListEffect>() {

    // TODO: Use DI in future
    private val recipesApi: RecipesApi = RecipeApiImpl()
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
                                recipe.copy(
                                    isLiked = !recipe.isLiked
                                )
                            } else {
                                recipe
                            }
                        }.toResource()
                    )
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
                updateState { state ->
                    state.copy(
                        recipesResource = Resource.Data(recipes)
                    )
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
                updateState { state ->
                    state.copy(
                        recipesResource = Resource.Data(
                            (state.recipesResource.value ?: emptyList()) + recipes
                        ),
                        isPaginationRecipesLoading = false,
                    )
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
}