package on.the.stove.presentation.recipesList

import on.the.stove.core.Resource
import on.the.stove.dto.Recipe

data class RecipesListState(
    val recipesResource: Resource<List<Recipe>> = Resource.Loading,
    val category: Int = 0,
    val page: Int = 0,
    val isPaginationRecipesLoading: Boolean = false,
    val isPaginationFull: Boolean = false,
)

sealed class RecipesListAction {

    object Init : RecipesListAction()

    object LoadNextPage : RecipesListAction()

    data class ChangeCategory(val category: Int = 0) : RecipesListAction()
}
