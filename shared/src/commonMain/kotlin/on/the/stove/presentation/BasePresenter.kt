package on.the.stove.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import on.the.stove.dispatchers.uiDispatcher
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<T>(
    private val coroutineContext: CoroutineContext
) {

    protected var view: T? = null
    protected lateinit var scope: PresenterCoroutineScope
    protected var uiScope = CoroutineScope(uiDispatcher)

    fun attachView(view: T) {
        scope = PresenterCoroutineScope(coroutineContext)
        this.view = view
        onViewAttached(view)
    }

    protected open fun onViewAttached(view: T) {}

    fun detachView() {
        view = null
        scope.viewDetached()
        onViewDetached()
    }

    protected open fun onViewDetached() {}
}

class PresenterCoroutineScope(
    context: CoroutineContext
) : CoroutineScope {

    private var onViewDetachJob = Job()
    override val coroutineContext: CoroutineContext = context + onViewDetachJob

    fun viewDetached() {
        onViewDetachJob.cancel()
    }
}