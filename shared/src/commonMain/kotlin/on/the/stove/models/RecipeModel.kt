package on.the.stove.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeModel(
    val id: String,
    @SerialName("photo") val imageUrl: String,
    @SerialName("name") val title: String,
    @SerialName("comment") val description: String,
    val author: String
)