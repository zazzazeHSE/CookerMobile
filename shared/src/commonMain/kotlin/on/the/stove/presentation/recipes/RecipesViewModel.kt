package on.the.stove.presentation.recipes

import on.the.stove.models.RecipeModel

data class RecipesViewModel (
    val recipes: List<RecipeModel>,
    val isFull: Boolean
)