package on.the.stove.android.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import on.the.stove.presentation.searchRecipes.SearchRecipesAction
import on.the.stove.presentation.searchRecipes.SearchRecipesState
import on.the.stove.presentation.searchRecipes.SearchRecipesStore

private const val PaginationOffset = 8

@Composable
internal fun SearchRecipesScreen(
    navHostController: NavHostController
) {
    val searchStore = remember { SearchRecipesStore() }
    LaunchedEffect(searchStore) {
        searchStore.reduce(SearchRecipesAction.Init)
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        val state: SearchRecipesState by searchStore.observeState().collectAsState()
        Text(
            "Найдем любой рецепт...",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 28.dp, bottom = 12.dp),
            fontWeight = FontWeight.Bold,
        )

        Box() {

        }
        TextField(
            modifier = Modifier
                .padding(bottom = 2.dp)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(corner = CornerSize(16.dp))
                ),
            value = state.searchQuery,
            onValueChange = { searchStore.reduce(SearchRecipesAction.SetSearchQuery(it)) },
            placeholder = { Text("Что Вы хотите найти?") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                disabledIndicatorColor = Color.White,
                errorIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
        )

        when (state.recipesResource) {
            is Resource.Data -> {
                val recipes = requireNotNull(state.recipesResource.value)

                val listState = rememberLazyListState()

                if (recipes.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(vertical = 60.dp),
                        text = "Тут пока пусто...",
                        textAlign = TextAlign.Center,
                    )
                } else {
                    FeedRecipeList(
                        state = listState, recipes = recipes,
                        onRecipeClick = { recipe ->
                            navHostController.navigate(
                                route = NavigationScreen.RecipeDetails.getRouterPath(recipeId = recipe.id),
                            )
                        },
                        onLikeClick = { recipe ->
                            searchStore.reduce(SearchRecipesAction.Like(recipe.id))
                        },
                    )
                }

                LaunchedEffect(listState) {
                    snapshotFlow { listState.firstVisibleItemIndex }
                        .map { index ->
                            (searchStore.observeState().first().recipesResource.value?.size
                                ?: 0) to index
                        }
                        .map { (size, index) -> size - PaginationOffset == index }
                        .distinctUntilChanged()
                        .filter { it }
                        .collect {
                            searchStore.reduce(SearchRecipesAction.LoadMore)
                        }
                }
            }
            else -> Unit
        }
    }
}