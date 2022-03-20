package on.the.stove.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import on.the.stove.android.theme.shimmerTheme
import on.the.stove.android.view.IngredientsList
import on.the.stove.core.Resource
import on.the.stove.dto.RecipeDetails
import on.the.stove.presentation.recipeDetails.RecipeDetailsAction
import on.the.stove.presentation.recipeNote.RecipeDetailsStore

@Composable
fun RecipeDetailsScreen(recipeId: String, onBack: () -> Unit) {
    val recipeDetailsStore = remember { RecipeDetailsStore() }

    LaunchedEffect(recipeDetailsStore) {
        recipeDetailsStore.reduce(RecipeDetailsAction.Init(id = recipeId))
    }

    val state = recipeDetailsStore.observeState().collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.value.recipeResource.value?.title ?: "",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBack.invoke() },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (state.value.recipeResource.isData) {
                        IconButton(
                            onClick = { recipeDetailsStore.reduce(RecipeDetailsAction.Like) },
                        ) {
                            val likeIcon = if (state.value.recipeResource.value!!.isLiked) {
                                Icons.Outlined.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            }
                            Icon(
                                imageVector = likeIcon,
                                contentDescription = "Favourite",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) {
        when (val recipeRes = state.value.recipeResource) {
            is Resource.Error -> {

            }
            is Resource.Loading -> {

            }
            is Resource.Data -> RecipeDetailsScreen(
                recipeRes.value,
                recipeDetailsStore = recipeDetailsStore
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RecipeDetailsScreen(details: RecipeDetails, recipeDetailsStore: RecipeDetailsStore) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        item {
            Card(
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 0.dp,
            ) {
                CoilImage(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(260.dp),
                    imageRequest = rememberImagePainter(
                        data = details.imageUrl,
                        builder = {
                            crossfade(true)
                        },
                    ).request,
                    contentScale = ContentScale.Crop,
                    contentDescription = details.title,
                    shimmerParams = shimmerTheme(),
                )
            }
        }
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = details.title,
                fontSize = 16.sp,
            )
        }
        item { RecipePager(details = details, recipeDetailsStore = recipeDetailsStore) }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RecipePager(details: RecipeDetails, recipeDetailsStore: RecipeDetailsStore) {
    val pagerState = rememberPagerState()

    // TODO: Use string resources
    val pages = listOf(
        "Описание", "Ингредиенты", "Рецепты"
    )
    TabRow(
        modifier = Modifier.padding(bottom = 8.dp),
        backgroundColor = Color.White,
        contentColor = MaterialTheme.colors.primary,
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        tabs = {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, fontSize = 12.sp) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            pagerState.scrollToPage(
                                index
                            )
                        }
                    },
                )
            }
        }
    )

    HorizontalPager(
        count = pages.size,
        state = pagerState,
        verticalAlignment = Alignment.Top,
    ) { page ->
        when (page) {
            0 -> Text(text = details.description)
            1 -> IngredientsList(ingredients = details.ingredients) { ingredient ->
                recipeDetailsStore.reduce(
                    RecipeDetailsAction.SelectIngredient(
                        ingredient = ingredient
                    )
                )
            }
            2 -> RecipeSteps(details = details)
        }
    }
}

@Composable
fun RecipeSteps(details: RecipeDetails) {
    Column() {
        details.steps.forEach { step ->
            Card(
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 0.dp,
            ) {
                CoilImage(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(180.dp),
                    imageRequest = rememberImagePainter(
                        data = step.imageUrl,
                        builder = {
                            crossfade(true)
                        },
                    ).request,
                    contentScale = ContentScale.Crop,
                    contentDescription = details.title,
                    shimmerParams = shimmerTheme(),
                )
            }

            step.steps.forEach { text ->
                Text(text = text)
            }
        }
    }
}