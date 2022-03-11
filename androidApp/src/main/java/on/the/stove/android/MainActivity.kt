package on.the.stove.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import on.the.stove.android.navigation.BottomNavigationScreens
import on.the.stove.android.screens.FavouritesScreen
import on.the.stove.android.screens.RecipesListScreen
import on.the.stove.android.theme.OnTheStoveTheme
import on.the.stove.presentation.recipesList.RecipesListStore


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnTheStoveTheme {
                MainScreen()
            }
        }
    }
}

val bottomNavigationItems = listOf(
    BottomNavigationScreens.Recipes,
    BottomNavigationScreens.Articles,
    BottomNavigationScreens.Cart,
    BottomNavigationScreens.Favorites,
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val recipesStore = remember { RecipesListStore() }
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavigationItems.forEach { screen ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    val selectedColor = if (selected) MaterialTheme.colors.primary else Color.Gray

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = null,
                                tint = selectedColor,
                            )
                        },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = selected,
                        selectedContentColor = selectedColor,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavigationScreens.Recipes.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationScreens.Recipes.route) { RecipesListScreen(recipesStore) }
            composable(BottomNavigationScreens.Favorites.route) { FavouritesScreen() }
            composable(BottomNavigationScreens.Cart.route) { }
            composable(BottomNavigationScreens.Articles.route) { }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}