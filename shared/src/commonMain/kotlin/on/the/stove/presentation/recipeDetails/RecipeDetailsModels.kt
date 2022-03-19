package on.the.stove.presentation.recipeDetails

import on.the.stove.core.Resource
import on.the.stove.dto.Ingredient
import on.the.stove.dto.RecipeDetails

data class RecipeDetailsState(
    val recipeId: String,
    val recipeResource: Resource<RecipeDetails> = Resource.Loading
)

sealed class RecipeDetailsAction {

    object Init : RecipeDetailsAction()

    object Like : RecipeDetailsAction()
    object OpenNote: RecipeDetailsAction()
    data class SetNoteContent(val content: String) : RecipeDetailsAction()

    data class SelectIngredient(val ingredient: Ingredient): RecipeDetailsAction()
}

sealed class RecipeDetailsEffect {

    data class OpenNote(val content: String): RecipeDetailsEffect()
}