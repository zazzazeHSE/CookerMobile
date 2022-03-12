package on.the.stove.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedRecipe(
    val id: String,
    @SerialName("comment") val description: String,
    @SerialName("name") val title: String,
    @SerialName("photo") val imageUrl: String,
    val ingredients: List<Ingredient>,
    val steps: List<Step>
)

@Serializable
data class Ingredient(
    val name: String,
    val value: String
)

@Serializable
data class Step(
    @SerialName("photo") val imageUrl: String,
    @SerialName("comment") val steps: List<String>
)