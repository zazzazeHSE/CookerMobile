package on.the.stove.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetails(
    val id: String,
    @SerialName("comment") val description: String,
    @SerialName("name") val title: String,
    @SerialName("photo") val imageUrl: String,
    val isLiked: Boolean = false,
    val ingredients: List<Ingredient>,
    val steps: List<Step>
) {
    val recipe: Recipe
        get() = Recipe(
            isLiked = isLiked,
            id = id,
            description = description,
            title = title,
            imageUrl = imageUrl,
        )
}

@Serializable
data class Ingredient(
    val name: String,
    val value: String,
    val inCart: Boolean = false
) {
    val id: String = "${name}_$value"
}

@Serializable
data class Step(
    @SerialName("photo") val imageUrl: String,
    @SerialName("comment") val steps: List<String>
)