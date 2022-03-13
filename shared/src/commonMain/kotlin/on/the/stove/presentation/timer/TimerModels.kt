package on.the.stove.presentation.timer

import on.the.stove.core.Resource

data class Timer(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)

data class TimerState(
    val timerResource: Resource<Timer> = Resource.Loading
)

sealed class TimerAction {
    object Tap: TimerAction()

    data class StartTimer(val timer: Timer): TimerAction()
}

sealed class TimerEffect {
    object ShowTimeSelector: TimerEffect()
    object HideTimeSelector: TimerEffect()
}