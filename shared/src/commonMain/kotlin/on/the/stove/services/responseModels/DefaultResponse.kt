package on.the.stove.services.responseModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import on.the.stove.models.RecipeModel

@Serializable
class DefaultResponse<out T: Any>(
    val code: Int,
    @SerialName("message") val body: List<T>
)