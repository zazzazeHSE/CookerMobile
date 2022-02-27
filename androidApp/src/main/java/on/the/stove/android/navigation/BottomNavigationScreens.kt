package on.the.stove.android.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import on.the.stove.android.R.string as strings

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {

    object Recipes : BottomNavigationScreens(
        route = "recipes",
        resourceId = strings.tab_name_recipes,
        Icons.Rounded.Dashboard
    )

    object Articles : BottomNavigationScreens(
        route = "articles",
        resourceId = strings.tab_name_article,
        Icons.Rounded.Article
    )

    object Cart : BottomNavigationScreens(
        route = "cart",
        resourceId = strings.tab_name_cart,
        Icons.Rounded.ShoppingCart
    )

    object Favorites : BottomNavigationScreens(
        route = "favourites",
        resourceId = strings.tab_name_favourites,
        Icons.Rounded.Favorite
    )
}