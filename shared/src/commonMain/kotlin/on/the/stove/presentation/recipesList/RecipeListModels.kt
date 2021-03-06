package on.the.stove.presentation.recipesList

import on.the.stove.core.Resource
import on.the.stove.dto.Category
import on.the.stove.dto.Recipe

data class RecipesListState(
    val recipesResource: Resource<List<Recipe>> = Resource.Loading,
    val categoriesResource: Resource<List<Category>> = Resource.Loading,
    val category: Category? = null,
    val page: Int = 1,
    val isPaginationRecipesLoading: Boolean = false,
    val isPaginationFull: Boolean = false,
)

sealed class RecipesListAction {

    object Init : RecipesListAction()

    object LoadNextPage : RecipesListAction()

    data class ChangeCategory(val category: Category) : RecipesListAction()

    data class Like(val id: String) : RecipesListAction()
}

sealed class RecipesListEffect {
    //
}