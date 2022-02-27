package on.the.stove.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import on.the.stove.android.navigation.BottomNavigationScreens
import on.the.stove.android.screens.RecipesListScreen
import on.the.stove.android.theme.OnTheStoveTheme


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
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavigationItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
            composable(BottomNavigationScreens.Recipes.route) { RecipesListScreen(navController) }
            composable(BottomNavigationScreens.Favorites.route) { TODO() }
            composable(BottomNavigationScreens.Cart.route) { TODO() }
            composable(BottomNavigationScreens.Articles.route) { TODO() }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}