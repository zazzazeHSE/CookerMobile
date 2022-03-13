package on.the.stove.android.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import on.the.stove.android.navigation.NavigationScreen
import on.the.stove.android.view.FeedRecipeList
import on.the.stove.core.Resource
import on.the.stove.presentation.recipesList.RecipesListAction
import on.the.stove.presentation.recipesList.RecipesListState
import on.the.stove.presentation.recipesList.RecipesListStore

private const val PaginationOffset = 8

@Composable
internal fun RecipesListScreen(
    navHostController: NavHostController
) {
    val recipesStore = remember { RecipesListStore() }
    LaunchedEffect(recipesStore) {
        recipesStore.reduce(RecipesListAction.Init)
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        val state: RecipesListState by recipesStore.observeState().collectAsState()

        CategoriesCarousel(recipesStore = recipesStore, recipesListState = state)

        when (state.recipesResource) {
            is Resource.Loading -> {
                // TODO
            }
            is Resource.Error -> {
                // TODO
            }
            is Resource.Data -> {
                val recipes = requireNotNull(state.recipesResource.value)

                val listState = rememberLazyListState()

                FeedRecipeList(
                    state = listState, recipes = recipes,
                    onRecipeClick = { recipe ->
                        navHostController.navigate(
                            route = NavigationScreen.RecipeDetails.getRouterPath(recipeId = recipe.id),
                        )
                    },
                    onLikeClick = { recipe ->
                        recipesStore.reduce(RecipesListAction.Like(recipe.id))
                    },
                )

                LaunchedEffect(listState) {
                    snapshotFlow { listState.firstVisibleItemIndex }
                        .map { index ->
                            (recipesStore.observeState().first().recipesResource.value?.size
                                ?: 0) to index
                        }
                        .map { (size, index) -> size - PaginationOffset == index }
                        .distinctUntilChanged()
                        .filter { it }
                        .collect {
                            recipesStore.reduce(RecipesListAction.LoadNextPage)
                        }
                }
            }
        }
    }
}

@Composable
private fun CategoriesCarousel(recipesListState: RecipesListState, recipesStore: RecipesListStore) {
    Text(
        "Какие рецепты вы желаете?",
        fontSize = 24.sp,
        modifier = Modifier.padding(top = 28.dp),
        fontWeight = FontWeight.Bold,
    )
    LazyRow(modifier = Modifier.padding(top = 4.dp)) {
        recipesListState.categoriesResource.value?.forEach { category ->
            item {
                val color = if (category.id == recipesListState.category?.id) {
                    MaterialTheme.colors.primary
                } else {
                    Color.Black
                }
                Text(
                    modifier = Modifier
                        .clickable {
                            recipesStore.reduce(
                                RecipesListAction.ChangeCategory(
                                    category = category
                                )
                            )
                        }
                        .padding(end = 8.dp),
                    text = category.name,
                    color = color,
                    fontSize = 16.sp,
                )
            }
        }
    }
}