package on.the.stove.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import on.the.stove.android.view.FeedRecipeList
import on.the.stove.core.Resource
import on.the.stove.presentation.favourites.FavouritesAction
import on.the.stove.presentation.favourites.FavouritesState
import on.the.stove.presentation.favourites.FavouritesStore

private val favouritesStore = FavouritesStore()

@Composable
internal fun FavouritesScreen() {
    LaunchedEffect(favouritesStore) {
        favouritesStore.reduce(FavouritesAction.Init)
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        val state: FavouritesState by favouritesStore.observeState().collectAsState()

        when (state.recipesResource) {
            is Resource.Data -> {
                val recipes = requireNotNull(state.recipesResource.value)

                FeedRecipeList(state = rememberLazyListState(), recipes = recipes) { recipe ->
                    favouritesStore.reduce(FavouritesAction.Like(recipe.id))
                }
            }
            else -> Unit
        }
    }
}