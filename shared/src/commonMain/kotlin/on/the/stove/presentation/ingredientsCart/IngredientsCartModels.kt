package on.the.stove.presentation.ingredientsCart

import on.the.stove.core.Resource
import on.the.stove.dto.Ingredient

data class IngredientsCartState(
    val ingredientsCartResource: Resource<List<Ingredient>> = Resource.Loading
)

sealed class IngredientsCartAction {
    object Init: IngredientsCartAction()

    data class Select(val ingredient: Ingredient): IngredientsCartAction()
}

sealed class IngredientsCartEffect {
    //
}
