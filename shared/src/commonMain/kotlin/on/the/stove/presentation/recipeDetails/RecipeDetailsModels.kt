package on.the.stove.presentation.recipeDetails

import on.the.stove.core.Resource
import on.the.stove.dto.RecipeDetails

data class RecipeDetailsState(
    val recipeResource: Resource<RecipeDetails> = Resource.Loading
)

sealed class RecipeDetailsAction {

    data class Init(val id: String) : RecipeDetailsAction()

    data class Like(val id: String) : RecipeDetailsAction()
}

sealed class RecipeDetailsEffect {
    //
}