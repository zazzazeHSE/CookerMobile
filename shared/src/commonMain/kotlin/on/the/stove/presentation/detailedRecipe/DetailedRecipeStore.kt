package on.the.stove.presentation.detailedRecipe

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import on.the.stove.core.Resource
import on.the.stove.presentation.BaseStore
import on.the.stove.services.network.RecipeApiImpl
import on.the.stove.services.network.RecipesApi

@OptIn(ObsoleteCoroutinesApi::class)
class DetailedRecipeStore:
    BaseStore<DetailedRecipeState, DetailedRecipeAction, DetailedRecipeEffect>() {
    // TODO: Use DI in future
    private val recipesApi: RecipesApi = RecipeApiImpl()
    override val stateFlow: MutableStateFlow<DetailedRecipeState> = MutableStateFlow(DetailedRecipeState())
    override val sideEffectsFlow: MutableSharedFlow<DetailedRecipeEffect> = MutableSharedFlow()

    override suspend fun reduce(action: DetailedRecipeAction, initialState: DetailedRecipeState) {
        when(action) {
            is DetailedRecipeAction.Init -> {
                updateState { state ->
                    state.copy(recipeResource = Resource.Loading)
                }
                loadRecipeDetails(action.id)
            }
            is DetailedRecipeAction.Like -> {
                // TODO: Add Like Action Handle
            }
        }
    }

    private suspend fun loadRecipeDetails(id: String) {
        recipesApi.getDetailedRecipe(id).fold(
            onSuccess = { result ->
                updateState { state ->
                    state.copy(recipeResource = Resource.Data(result))
                }
            },
            onFailure = { error ->
                updateState { state ->
                    state.copy(recipeResource = Resource.Error(error))
                }
            }
        )
    }
}