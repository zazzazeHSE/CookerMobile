package on.the.stove.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String,
    @SerialName("photo") val imageUrl: String,
    @SerialName("name") val title: String,
    @SerialName("comment") val description: String,
    val author: String
)