package on.the.stove.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: Must be renamed to RecipeShort or RecipeFeed
@Serializable
data class Recipe(
    val id: String,
    @SerialName("photo") val imageUrl: String,
    @SerialName("name") val title: String,
    @SerialName("comment") val description: String,
    val author: String,
    val isLiked: Boolean = false,
)