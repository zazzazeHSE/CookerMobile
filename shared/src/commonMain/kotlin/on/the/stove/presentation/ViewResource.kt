package on.the.stove.presentation

sealed class ViewResource<out T: Any> {
    open val value: T? = null
    open val throwable: Throwable? = null

    object Loading : ViewResource<Nothing>()
    data class Data<T : Any>(override val value: T) : ViewResource<T>()
    data class Error(override val throwable: Throwable) : ViewResource<Nothing>()
}