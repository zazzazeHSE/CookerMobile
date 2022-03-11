package on.the.stove.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import on.the.stove.android.view.FeedRecipeList
import on.the.stove.core.Resource
import on.the.stove.presentation.recipesList.RecipesListAction
import on.the.stove.presentation.recipesList.RecipesListState
import on.the.stove.presentation.recipesList.RecipesListStore

private const val PaginationOffset = 8
private var isInit = false

@Composable
internal fun RecipesListScreen(recipesStore: RecipesListStore) {
    LaunchedEffect(isInit) {
        if (!isInit) recipesStore.reduce(RecipesListAction.Init)

        // TODO: research this case
        isInit = true
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        val state: RecipesListState by recipesStore.observeState().collectAsState()

        when (state.recipesResource) {
            is Resource.Loading -> {

            }
            is Resource.Error -> {

            }
            is Resource.Data -> {
                val recipes = requireNotNull(state.recipesResource.value)

                val listState = rememberLazyListState()

                FeedRecipeList(state = listState, recipes = recipes) { recipe ->
                    recipesStore.reduce(RecipesListAction.Like(recipe.id))
                }

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