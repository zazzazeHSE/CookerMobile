package on.the.stove.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import on.the.stove.android.theme.shimmerTheme
import on.the.stove.core.Resource
import on.the.stove.dto.Recipe
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

                FeedRecipeList(recipesStore = recipesStore, recipes = recipes)
            }
        }
    }
}

@Composable
private fun FeedRecipeList(recipesStore: RecipesListStore, recipes: List<Recipe>) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        recipes.forEach { recipe ->
            item {
                FeedRecipe(
                    feedRecipe = recipe,
                    onLike = { recipesStore.reduce(RecipesListAction.Like(recipe.id)) })
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index ->
                (recipesStore.observeState().first().recipesResource.value?.size ?: 0) to index
            }
            .map { (size, index) -> size - PaginationOffset == index }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                recipesStore.reduce(RecipesListAction.LoadNextPage)
            }
    }
}

@Composable
private fun FeedRecipe(feedRecipe: Recipe, onLike: () -> Unit) {
    Card(modifier = Modifier, shape = RoundedCornerShape(16.dp), elevation = 2.dp) {
        Box(contentAlignment = Alignment.TopEnd) {
            Column {
                CoilImage(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(160.dp),
                    imageRequest = rememberImagePainter(
                        data = feedRecipe.imageUrl,
                        builder = {
                            crossfade(true)
                        },
                    ).request,
                    contentScale = ContentScale.Crop,
                    contentDescription = feedRecipe.title,
                    shimmerParams = shimmerTheme(),
                )
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = feedRecipe.title,
                    fontSize = 16.sp,
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colors.primary)
            ) {
                IconToggleButton(
                    modifier = Modifier.padding(4.dp),
                    checked = feedRecipe.isLiked,
                    onCheckedChange = {
                        onLike.invoke()
                    }
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = if (feedRecipe.isLiked) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = null
                    )
                }
            }
        }
    }
}
