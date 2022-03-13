package on.the.stove.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import on.the.stove.android.view.IngredientsList
import on.the.stove.core.Resource
import on.the.stove.presentation.ingredientsCart.IngredientsCartAction
import on.the.stove.presentation.ingredientsCart.IngredientsCartState
import on.the.stove.presentation.ingredientsCart.IngredientsCartStore

private val store = IngredientsCartStore()

@Composable
internal fun IngredientsCardScreen() {
    LaunchedEffect(store) {
        store.reduce(IngredientsCartAction.Init)
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        val state: IngredientsCartState by store.observeState().collectAsState()

        Text(
            "Корзина продуктов",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 28.dp),
            fontWeight = FontWeight.Bold,
        )
        when (val resource = state.ingredientsCartResource) {
            is Resource.Data -> {
                IngredientsList(ingredients = resource.value, onSelect = { ingredient ->
                    store.reduce(IngredientsCartAction.Select(ingredient))
                })
            }
            else -> Unit
        }
    }
}