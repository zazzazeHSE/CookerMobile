package on.the.stove.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dispatchers.uiDispatcher

@OptIn(ObsoleteCoroutinesApi::class)
abstract class BaseStore<State, Action, Effect> {

    @Deprecated("Use flow")
    private var updateCallback: ((state: State) -> Unit)? = null

    protected abstract val stateFlow: MutableStateFlow<State>
    protected abstract val sideEffectsFlow: MutableSharedFlow<Effect>

    private val scope: CoroutineScope = CoroutineScope(uiDispatcher + SupervisorJob())

    protected suspend fun updateState(reduceState: (state: State) -> State) =
        stateFlow.emit(reduceState(stateFlow.value))

    protected abstract suspend fun reduce(action: Action, initialState: State)

    // TODO: rename to dispatch or accept
    fun reduce(action: Action) {
        scope.launch(ioDispatcher) {
            reduce(action, stateFlow.value)
        }
    }

    @Deprecated("Only for support iOS", ReplaceWith("observeState"))
    fun attachView(updateCallback: (state: State) -> Unit) {
        this.updateCallback = updateCallback

        scope.launch {
            stateFlow.collect { state ->
                updateCallback(state)
            }
        }
    }

    fun observeState(): StateFlow<State> = stateFlow

    fun observeSideEffects(): Flow<Effect> = sideEffectsFlow
}
