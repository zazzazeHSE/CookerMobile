package on.the.stove.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import on.the.stove.android.navigation.NavigationScreen
import on.the.stove.android.view.FeedRecipeList
import on.the.stove.core.Resource
import on.the.stove.presentation.favourites.FavouritesAction
import on.the.stove.presentation.favourites.FavouritesState
import on.the.stove.presentation.favourites.FavouritesStore

private val favouritesStore = FavouritesStore()

@Composable
internal fun FavouritesScreen(navHostController: NavHostController) {
    LaunchedEffect(favouritesStore) {
        favouritesStore.reduce(FavouritesAction.Init)
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        val state: FavouritesState by favouritesStore.observeState().collectAsState()

        Text(
            "Любимые рецепты",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 28.dp),
            fontWeight = FontWeight.Bold,
        )
        when (state.recipesResource) {
            is Resource.Data -> {
                val recipes = requireNotNull(state.recipesResource.value)

                FeedRecipeList(
                    state = rememberLazyListState(), recipes = recipes,
                    onRecipeClick = { recipe ->
                        navHostController.navigate(
                            route = NavigationScreen.RecipeDetails.getRouterPath(recipeId = recipe.id),
                        )
                    },
                    onLikeClick = { recipe ->
                        favouritesStore.reduce(FavouritesAction.Like(recipe.id))
                    },
                )
            }
            else -> Unit
        }
    }
}