package on.the.stove.android.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import on.the.stove.android.R.string as strings

interface NavigationCommand {

    val destinationTemplate: String

    val destinationArguments: List<NamedNavArgument>

}

sealed class NavigationScreen(open val route: String) {

    sealed class BottomNavigationScreen(
        override val route: String,
        @StringRes val resourceId: Int,
        val icon: ImageVector
    ) : NavigationScreen(route) {

        object Recipes : BottomNavigationScreen(
            route = "recipes",
            resourceId = strings.tab_name_recipes,
            Icons.Rounded.Dashboard
        )

        object Cart : BottomNavigationScreen(
            route = "cart",
            resourceId = strings.tab_name_cart,
            Icons.Rounded.ShoppingCart
        )

        object Favorites : BottomNavigationScreen(
            route = "favourites",
            resourceId = strings.tab_name_favourites,
            Icons.Rounded.Favorite
        )

        object Search : BottomNavigationScreen(
            route = "search",
            resourceId = strings.tab_name_search,
            Icons.Rounded.Search,
        )
    }

    object RecipeDetails : NavigationScreen(route = "recipeDetails"), NavigationCommand {

        override val destinationTemplate = "$route/{recipeId}"

        override val destinationArguments = listOf(
            navArgument(name = "recipeId", builder = { type = NavType.StringType })
        )

        fun getRouterPath(recipeId: String) = "$route/${recipeId}"

        val firstArgument = "recipeId" to ""
    }
}
