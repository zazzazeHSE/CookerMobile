package on.the.stove.presentation.recipes

import on.the.stove.presentation.ViewResource

interface IRecipesView {
    fun updateWithResource(resource: ViewResource<RecipesViewModel>)
}