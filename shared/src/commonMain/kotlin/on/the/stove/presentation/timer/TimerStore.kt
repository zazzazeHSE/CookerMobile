package on.the.stove.presentation.timer

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import on.the.stove.core.Resource
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.presentation.BaseStore


class TimerStore : BaseStore<TimerState, TimerAction, TimerEffect>() {
    override val stateFlow: MutableStateFlow<TimerState> = MutableStateFlow(TimerState())
    override val sideEffectsFlow: MutableSharedFlow<TimerEffect> = MutableSharedFlow()

    private var timerJob: Job? = null

    override suspend fun reduce(action: TimerAction, initialState: TimerState) {
        when (action) {
            is TimerAction.Tap -> {
                timerJob?.let { job ->
                    job.cancel()
                    timerJob = null
                    updateState { state ->
                        state.copy(timerResource = Resource.Loading)
                    }
                } ?: sideEffectsFlow.emit(TimerEffect.ShowTimeSelector)
            }
            is TimerAction.StartTimer -> {
                val seconds: Int =
                    action.timer.hours * 3600 + action.timer.minutes * 60 + action.timer.seconds

                timerJob = scope.launch(ioDispatcher) {
                    ((seconds - 1) downTo 0).asFlow().onEach { delay(1000) }.collect { second ->
                        if (second == 0) {
                            updateState { state ->
                                state.copy(timerResource = Resource.Loading)
                            }
                            this.cancel()
                            return@collect
                        }
                        val timer = Timer(
                            hours = second / 3600,
                            minutes = second / 60 % 60,
                            seconds = second % 60,
                        )
                        updateState { state ->
                            state.copy(timerResource = Resource.Data(timer))
                        }
                    }
                }
                sideEffectsFlow.emit(TimerEffect.HideTimeSelector)
                updateState { state ->
                    state.copy(timerResource = Resource.Data(action.timer))
                }
            }
        }
    }
}
