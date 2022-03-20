@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package on.the.stove.presentation.searchRecipes

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import on.the.stove.core.Resource
import on.the.stove.core.toResource
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Recipe
import on.the.stove.presentation.BaseStore
import on.the.stove.services.network.RecipesApi
import org.koin.core.component.inject

class SearchRecipesStore :
    BaseStore<SearchRecipesState, SearchRecipesAction, SearchRecipesEffect>() {

    private val recipesApi: RecipesApi by inject()
    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override val stateFlow: MutableStateFlow<SearchRecipesState> =
        MutableStateFlow(SearchRecipesState())
    override val sideEffectsFlow: MutableSharedFlow<SearchRecipesEffect> = MutableSharedFlow()

    private val searchQuerySharedFlow = MutableSharedFlow<String>()
    private val searchQueryFlow = searchQuerySharedFlow
        .distinctUntilChanged()
        .debounce(600L)

    private var loadMoreJob: Job? = null

    init {
        scope.launch(ioDispatcher) {
            searchQueryFlow
                .filter { searchQuery -> searchQuery.isNotBlank() }
                .map { searchQuery -> searchQuery.trim() }
                .flatMapLatest { searchQuery ->
                    loadMoreJob?.cancel()

                    updateState { state ->
                        state.copy(
                            recipesResource = Resource.Loading,
                            isPaginationLoading = true,
                            page = 1,
                            searchQuery = searchQuery,
                        )
                    }

                    val state = stateFlow.value

                    flowOf(
                        recipesApi.getSearchRecipesList(
                            searchQuery = state.searchQuery,
                            page = state.page
                        )
                    )
                }.collectLatest { result ->
                    result.fold(
                        onSuccess = { recipes ->
                            recipes.mergeCachedAndInvoke {
                                updateState { state ->
                                    state.copy(
                                        recipesResource = it.toResource(),
                                        isPaginationLoading = false,
                                    )
                                }
                            }
                        },
                        onFailure = {
                            updateState { state ->
                                state.copy(
                                    recipesResource = it.toResource(),
                                    isPaginationLoading = false,
                                )
                            }
                        }
                    )
                }
        }
    }

    override suspend fun reduce(action: SearchRecipesAction, initialState: SearchRecipesState) {
        when (action) {
            is SearchRecipesAction.Init -> {
                observeFavouritesRecipes()
            }
            is SearchRecipesAction.SetSearchQuery -> {
                updateState { state ->
                    state.copy(
                        searchQuery = action.searchQuery,
                    )
                }
                searchQuerySharedFlow.emit(action.searchQuery)
            }
            is SearchRecipesAction.Like -> {
                val recipe = stateFlow.value.recipesResource.value!!.first { recipe ->
                    recipe.id == action.recipeId
                }

                if (recipe.isLiked) {
                    appDatabaseRepository.removeFavouriteRecipe(recipe.id)
                } else {
                    appDatabaseRepository.addFavouriteRecipe(recipe)
                }
            }
            is SearchRecipesAction.LoadMore -> {
                loadMoreJob = scope.launch(ioDispatcher) {
                    updateState { state ->
                        state.copy(
                            page = state.page + 1,
                            isPaginationLoading = true,
                            isPaginationError = false,
                        )
                    }

                    val state = stateFlow.value

                    recipesApi.getSearchRecipesList(
                        searchQuery = state.searchQuery,
                        page = state.page,
                    ).fold(
                        onSuccess = { newRecipes ->
                            val isFull = newRecipes.isEmpty()
                            newRecipes.mergeCachedAndInvoke {
                                updateState { state ->
                                    state.copy(
                                        recipesResource = (state.recipesResource.value!! + it).toResource(),
                                        isPaginationLoading = false,
                                        isPaginationError = false,
                                        isPaginationFull = isFull,
                                    )
                                }
                            }
                        },
                        onFailure = {
                            updateState { state ->
                                state.copy(
                                    isPaginationLoading = false,
                                    isPaginationError = true,
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private fun observeFavouritesRecipes() = scope.launch(ioDispatcher) {
        appDatabaseRepository.observeAllFavouritesRecipes()
            .collect { favouriteRecipes ->
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

    private suspend inline fun List<Recipe>.mergeCachedAndInvoke(crossinline andThen: suspend (mergedRecipes: List<Recipe>) -> Unit) {
        val dbRecipes = appDatabaseRepository.getAllFavouritesRecipes()

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