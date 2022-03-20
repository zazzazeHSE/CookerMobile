package on.the.stove.presentation.favourites

import on.the.stove.core.Resource
import on.the.stove.dto.Recipe

data class FavouritesState(
    val recipesResource: Resource<List<Recipe>> = Resource.Loading,
)

sealed class FavouritesAction {

    object Init : FavouritesAction()

    data class Like(val id: String) : FavouritesAction()
}

sealed class FavouritesEffect {
    //
}