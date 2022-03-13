package on.the.stove.android.view

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import on.the.stove.presentation.timer.Timer
import on.the.stove.presentation.timer.TimerAction
import on.the.stove.presentation.timer.TimerState
import on.the.stove.presentation.timer.TimerStore
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimerFloatingAction(
    timerStore: TimerStore,
    timerState: State<TimerState>,
    fragmentManager: FragmentManager,
) {
    val timerValue = timerState.value.timerResource.value

    if (timerValue != null) {
        val time = LocalTime.of(timerValue.hours, timerValue.minutes, timerValue.seconds)
        val timeFormatted = time.format(DateTimeFormatter.ISO_LOCAL_TIME)

        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Вы точно хотите отметить таймер?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            timerStore.reduce(TimerAction.Tap)
                        }) {
                        Text("Отменить таймер", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Закрыть", color = Color.White)
                    }
                }
            )
        }

        ExtendedFloatingActionButton(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            text = { Text(text = timeFormatted) },
            onClick = {
                openDialog.value = true
            },
            icon = {
                Icon(
                    Icons.Rounded.Timer,
                    contentDescription = "Timer",
                    tint = Color.White
                )
            }
        )
    } else {
        FloatingActionButton(
            onClick = {
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(0)
                    .setMinute(10)
                    .setTitleText("Выберите значение для таймера")
                    .build()
                picker.addOnPositiveButtonClickListener {
                    timerStore.reduce(
                        TimerAction.StartTimer(
                            // because MaterialTimePicker not support seconds
                            timer = Timer(
                                hours = 0,
                                minutes = picker.hour,
                                seconds = picker.minute,
                            )
                        )
                    )
                }
                picker.show(fragmentManager, null)
            },
            backgroundColor = MaterialTheme.colors.primary,
        ) {
            Icon(
                Icons.Rounded.Timer,
                contentDescription = "Timer",
                tint = Color.White
            )
        }
    }
}
