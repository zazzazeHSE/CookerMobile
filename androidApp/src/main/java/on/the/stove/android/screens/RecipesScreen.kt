package on.the.stove.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import on.the.stove.android.theme.shimmerTheme
import java.util.*

@Composable
internal fun RecipesListScreen() {
    Column(Modifier.padding(horizontal = 16.dp)) {
        var recipes by remember { mutableStateOf(MockRecipes) }

        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            recipes.forEach { feedRecipe ->
                item {
                    FeedRecipe(feedRecipe = feedRecipe, onLike = {
                        recipes = recipes.map { recipe ->
                            if (recipe.id == feedRecipe.id) {
                                recipe.copy(
                                    isFavorite = !recipe.isFavorite
                                )
                            } else {
                                recipe
                            }
                        }
                    })
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun FeedRecipe(feedRecipe: FeedRecipe, onLike: () -> Unit) {
    Card(modifier = Modifier, elevation = 2.dp) {
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
                    .padding(4.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colors.primary)
            ) {
                IconToggleButton(
                    checked = feedRecipe.isFavorite,
                    onCheckedChange = {
                        onLike.invoke()
                    }
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = if (feedRecipe.isFavorite) {
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

data class FeedRecipe(
    val id: String,
    val title: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
)

private val MockRecipes = listOf(
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Сырная лепёшка с картошкой",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/36/2951035_28571.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
    FeedRecipe(
        id = UUID.randomUUID().toString(),
        title = "Бриошь \"Карнавальная\"",
        imageUrl = "https://www.povarenok.ru/data/cache/2022feb/27/32/2951000_61620.jpg"
    ),
)