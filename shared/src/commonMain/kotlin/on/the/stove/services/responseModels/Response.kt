package on.the.stove.services.responseModels

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class Response<out T : Any>(
    val code: Int,
    @SerialName("message") val payload: T,
)

