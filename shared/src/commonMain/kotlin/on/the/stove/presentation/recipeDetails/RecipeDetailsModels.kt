package on.the.stove.presentation.recipeDetails

import on.the.stove.core.Resource
import on.the.stove.dto.Ingredient
import on.the.stove.dto.RecipeDetails

data class RecipeDetailsState(
    val recipeResource: Resource<RecipeDetails> = Resource.Loading
)

sealed class RecipeDetailsAction {

    data class Init(val id: String) : RecipeDetailsAction()

    object Like : RecipeDetailsAction()

    data class SelectIngredient(val ingredient: Ingredient): RecipeDetailsAction()
}

sealed class RecipeDetailsEffect {
    //
}