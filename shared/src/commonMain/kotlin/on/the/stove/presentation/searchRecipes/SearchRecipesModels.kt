package on.the.stove.presentation.searchRecipes

import on.the.stove.core.Resource
import on.the.stove.dto.Recipe

data class SearchRecipesState(
    val recipesResource: Resource<List<Recipe>> = Resource.Data(emptyList()),
    val isPaginationLoading: Boolean = false,
    val isPaginationError: Boolean = false,
    val page: Int = 1,
    val searchQuery: String = "",
    val isPaginationFull: Boolean = false
)

sealed class SearchRecipesAction {

    object Init : SearchRecipesAction()

    data class SetSearchQuery(val searchQuery: String) : SearchRecipesAction()
    data class Like(val recipeId: String) : SearchRecipesAction()
    object LoadMore : SearchRecipesAction()
}

sealed class SearchRecipesEffect {
    // Empty
}

