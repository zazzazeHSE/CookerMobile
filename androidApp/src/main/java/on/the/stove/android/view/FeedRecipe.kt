package on.the.stove.android.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.skydoves.landscapist.coil.CoilImage
import on.the.stove.android.theme.shimmerTheme
import on.the.stove.dto.Recipe

@Composable
internal fun FeedRecipeList(
    state: LazyListState,
    recipes: List<Recipe>,
    onLikeClick: (recipe: Recipe) -> Unit,
    onRecipeClick: (recipe: Recipe) -> Unit,
) {
    LazyColumn(
        state = state,
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        recipes.forEach { recipe ->
            item {
                FeedRecipe(
                    feedRecipe = recipe,
                    onLike = { onLikeClick.invoke(recipe) },
                    onRecipeClick = { onRecipeClick.invoke(recipe) }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun FeedRecipe(feedRecipe: Recipe, onLike: () -> Unit, onRecipeClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable { onRecipeClick.invoke() },
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp
    ) {
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
