package on.the.stove.core

sealed class Resource<out T: Any> {
    open val value: T? = null
    open val throwable: Throwable? = null

    object Loading : Resource<Nothing>()
    data class Data<T : Any>(override val value: T) : Resource<T>()
    data class Error(override val throwable: Throwable) : Resource<Nothing>()
}