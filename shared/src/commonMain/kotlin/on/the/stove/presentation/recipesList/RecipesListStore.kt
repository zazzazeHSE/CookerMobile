package on.the.stove.presentation.recipesList

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
import on.the.stove.services.network.CategoriesApi
import on.the.stove.services.network.RecipesApi
import org.koin.core.component.inject

@OptIn(ObsoleteCoroutinesApi::class)
class RecipesListStore :
    BaseStore<RecipesListState, RecipesListAction, RecipesListEffect>() {

    private val recipesApi: RecipesApi by inject()
    private val categoriesApi: CategoriesApi by inject()
    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override val stateFlow: MutableStateFlow<RecipesListState> =
        MutableStateFlow(RecipesListState())
    override val sideEffectsFlow: MutableSharedFlow<RecipesListEffect> = MutableSharedFlow()

    override suspend fun reduce(action: RecipesListAction, initialState: RecipesListState) {
        when (action) {
            is RecipesListAction.Init -> {
                observeFavouritesRecipes()
                loadCategories()

                when (val categoriesRes = stateFlow.value.categoriesResource) {
                    is Resource.Data -> {
                        val categories = categoriesRes.value

                        if (categories.isEmpty()) {
                            updateState { state ->
                                state.copy(
                                    categoriesResource = IllegalArgumentException("Empty categories").toResource()
                                )
                            }
                        } else {
                            updateState { state ->
                                state.copy(
                                    category = categories.first()
                                )
                            }
                        }

                    }
                    else -> return
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
                    appDatabaseRepository.removeFavouriteRecipe(recipe.id)
                } else {
                    appDatabaseRepository.addFavouriteRecipe(recipe)
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

    private suspend fun loadCategories() {
        categoriesApi.getCategories().fold(
            onSuccess = { categories ->
                updateState { state ->
                    state.copy(
                        categoriesResource = categories.toResource()
                    )
                }
            },
            onFailure = { error ->
                updateState { state ->
                    state.copy(
                        categoriesResource = error.toResource()
                    )
                }
            }
        )
    }

    private suspend fun loadRecipes(state: RecipesListState) {
        recipesApi.getRecipesList(
            page = state.page,
            category = requireNotNull(state.category),
        ).fold(
            onSuccess = { recipes ->
                recipes.mergeCachedAndInvoke { mergedRecipes ->
                    updateState { state ->
                        state.copy(
                            recipesResource = mergedRecipes.toResource()
                        )
                    }
                }
            },
            onFailure = { error ->
                updateState { state ->
                    state.copy(
                        recipesResource = error.toResource()
                    )
                }
            }
        )
    }

    private suspend fun loadNextPage(state: RecipesListState) {
        recipesApi.getRecipesList(
            page = state.page,
            category = requireNotNull(state.category),
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