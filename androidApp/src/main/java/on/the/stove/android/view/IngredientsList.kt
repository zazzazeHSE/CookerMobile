package on.the.stove.android.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import on.the.stove.dto.Ingredient

@Composable
fun IngredientsList(ingredients: List<Ingredient>, onSelect: (ingredient: Ingredient) -> Unit) {
    Column() {
        ingredients.forEach { ingredient ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(1f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = ingredient.name,
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = ingredient.value,
                    textAlign = TextAlign.End
                )
                Checkbox(
                    modifier = Modifier.padding(start = 4.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.primary,
                        checkmarkColor = Color.White,
                        uncheckedColor = MaterialTheme.colors.primary,
                    ),
                    checked = ingredient.inCart,
                    onCheckedChange = {
                        onSelect.invoke(ingredient)
                    }
                )
            }
        }
    }
}