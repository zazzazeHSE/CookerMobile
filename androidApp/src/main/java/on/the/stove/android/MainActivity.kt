package on.the.stove.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import on.the.stove.android.ext.requireGetString
import on.the.stove.android.navigation.NavigationScreen.BottomNavigationScreen
import on.the.stove.android.navigation.NavigationScreen.RecipeDetails
import on.the.stove.android.screens.FavouritesScreen
import on.the.stove.android.screens.IngredientsCardScreen
import on.the.stove.android.screens.RecipeDetailsScreen
import on.the.stove.android.screens.RecipesListScreen
import on.the.stove.android.theme.OnTheStoveTheme
import on.the.stove.android.view.TimerFloatingAction
import on.the.stove.presentation.timer.TimerStore

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnTheStoveTheme {
                MainScreen(supportFragmentManager)
            }
        }
    }
}

val bottomNavigationItems = listOf(
    BottomNavigationScreen.Recipes,
    BottomNavigationScreen.Cart,
    BottomNavigationScreen.Favorites,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(fragmentManager: FragmentManager) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val timerStore = remember { TimerStore() }
    val timerState = timerStore.observeState().collectAsState()
    Scaffold(
        floatingActionButton = {
            TimerFloatingAction(timerStore, timerState, fragmentManager)
        },
        bottomBar = {
            if (!bottomNavigationItems.map(BottomNavigationScreen::route)
                    .contains(currentDestination?.route)
            ) {
                return@Scaffold
            }

            BottomNavigation(
                backgroundColor = Color.White
            ) {
                bottomNavigationItems.forEach { screen ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    val selectedColor =
                        if (selected) MaterialTheme.colors.primary else Color.Gray

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
            startDestination = BottomNavigationScreen.Recipes.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationScreen.Recipes.route) {
                RecipesListScreen(
                    navHostController = navController
                )
            }
            composable(BottomNavigationScreen.Favorites.route) {
                FavouritesScreen(
                    navHostController = navController
                )
            }
            composable(BottomNavigationScreen.Cart.route) { IngredientsCardScreen() }
            composable(
                route = RecipeDetails.destinationTemplate,
                arguments = RecipeDetails.destinationArguments,
            ) { navBackStackEntry ->
                RecipeDetailsScreen(
                    recipeId = navBackStackEntry.arguments.requireGetString(
                        key = RecipeDetails.firstArgument.first,
                        defaultValue = RecipeDetails.firstArgument.second
                    ),
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
