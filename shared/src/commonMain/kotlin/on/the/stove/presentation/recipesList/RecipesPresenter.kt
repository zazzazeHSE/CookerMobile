package on.the.stove.presentation.recipesList

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import on.the.stove.core.Resource
import on.the.stove.presentation.BasePresenter
import on.the.stove.services.apiServices.RecipesService

@OptIn(ObsoleteCoroutinesApi::class)
class RecipesPresenter : BasePresenter<RecipesListState, RecipesListAction>() {
    private val recipesService = RecipesService()

    init {
        ioScope.launch {
            actionsFlow
                .flowOn(ioScope.coroutineContext)
                .collect { action ->
                    // TODO: This logic must be implemented in reducer entity, but now it is overhead
                    when (action) {
                        is RecipesListAction.Init -> {
                            // TODO: Load first page with default params without pagination
                        }
                        is RecipesListAction.LoadNextPage -> {
                            loadNextPage()
                        }
                        is RecipesListAction.ChangeCategory -> {
                            updateState { state ->
                                state.copy(
                                    category = action.category
                                )
                            }

                            // TODO: invoke load first page. The same logic must be in RecipesListAction.Init
                        }
                    }
                }
        }
    }

    private suspend fun loadNextPage() {
        val currentState = updateState { state ->
            state.copy(
                page = state.page + 1,
                isPaginationRecipesLoading = true,
            )
        }

        recipesService.loadRecipes(
            category = currentState.category,
            page = currentState.page,
        ) { result ->
            result.onSuccess { newRecipes ->
                ioScope.launch {
                    updateState { state ->
                        state.copy(
                            recipesResource = Resource.Data(
                                (state.recipesResource.value ?: emptyList()) + newRecipes
                            ),
                            isPaginationRecipesLoading = false,
                        )
                    }
                }
            }
            result.onFailure {
                ioScope.launch {
                    updateState { state ->
                        state.copy(
                            isPaginationRecipesLoading = false,
                        )
                    }
                }
            }
        }
    }
}