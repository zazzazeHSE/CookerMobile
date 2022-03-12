package on.the.stove.presentation.detailedRecipe

import on.the.stove.core.Resource
import on.the.stove.dto.DetailedRecipe

data class DetailedRecipeState(
    val recipeResource: Resource<DetailedRecipe> = Resource.Loading
)

sealed class DetailedRecipeAction {

    data class Init(val id: String) : DetailedRecipeAction()

    data class Like(val id: String) : DetailedRecipeAction()
}

sealed class DetailedRecipeEffect {
    //
}