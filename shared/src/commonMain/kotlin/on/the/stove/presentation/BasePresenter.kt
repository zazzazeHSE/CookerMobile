package on.the.stove.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dispatchers.uiDispatcher

@OptIn(ObsoleteCoroutinesApi::class)
abstract class BasePresenter<State, Action> {

    private var updateCallback: ((state: State) -> Unit)? = null
    private val stateChannel = BroadcastChannel<State>(1)
    private val actionsChannel = BroadcastChannel<Action>(1)

    private val stateFlow: Flow<State> = stateChannel.openSubscription().receiveAsFlow()
    protected val actionsFlow : Flow<Action> = actionsChannel.openSubscription().receiveAsFlow()

    private val uiScope = CoroutineScope(uiDispatcher)
    protected val ioScope = CoroutineScope(ioDispatcher)

    init {
        uiScope.launch {
            stateFlow
                .flowOn(uiScope.coroutineContext)
                .distinctUntilChanged()
                .collect { state -> updateCallback?.invoke(state) }
        }
    }

    protected suspend fun updateState(reduceState: (state: State) -> State): State {
        // TODO: Perhaps the condition of the race
        val reducedState = stateFlow.firstOrNull()?.let(reduceState)
            ?: error("Explore the state when there is no time when updating")

        stateChannel.send(reducedState)

        return reducedState
    }

    fun attachView(updateCallback: (state: State) -> Unit) {
        this.updateCallback = updateCallback
    }

    fun detachView() {
        this.updateCallback = null
    }

    fun reduce(action: Action) {
        ioScope.launch {
            actionsChannel.send(action)
        }
    }
}
