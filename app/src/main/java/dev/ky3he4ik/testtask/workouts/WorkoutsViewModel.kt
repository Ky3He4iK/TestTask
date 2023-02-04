package dev.ky3he4ik.testtask.workouts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ky3he4ik.testtask.navigation.RouteNavigator
import dev.ky3he4ik.testtask.util.MyDateUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class WorkoutsViewModel @Inject constructor(private val routeNavigator: RouteNavigator) :
    ViewModel(), RouteNavigator by routeNavigator {

    data class Workout(
        val start: Date,
        val duration: Duration,
        val name: String,
    )

    data class WorkoutsState(
        val workouts: List<Workout>,
        val isRunning: Boolean,
        val savedTimeStr: List<String>
    )

    var viewState by mutableStateOf(
        WorkoutsState(listOf(), false, listOf())
    )

    var elapsedTimeStr by mutableStateOf(MyDateUtil.formatTime(0))


    fun addWorkout(start: Date, duration: Duration, name: String) {
        val workout = Workout(start, duration, name)
        viewState = viewState.copy(
            workouts = (viewState.workouts + listOf(workout)).sortedBy(Workout::start)
        )
    }

    private var elapsedTime by mutableStateOf<Long>(0)
    private var lastTimestamp: Long = 0
    private var currTimestamp: Long = 0

    fun start() {
        if (viewState.isRunning)
            return
        runStopWatch()
    }

    fun nextLoop() {
        viewState =
            viewState.copy(
                savedTimeStr = viewState.savedTimeStr + listOf(
                    MyDateUtil.formatTime(
                        elapsedTime
                    )
                )
            )
    }

    fun stop() {
        viewState = viewState.copy(isRunning = false)
    }

    fun reset() {
        viewState = viewState.copy(isRunning = false, savedTimeStr = listOf())
        elapsedTime = 0
        elapsedTimeStr = MyDateUtil.formatTime(0)
    }

    private fun runStopWatch() {
        viewState = viewState.copy(isRunning = true)
        viewModelScope.launch {
            lastTimestamp = System.currentTimeMillis()

            while (viewState.isRunning) {
                currTimestamp = System.currentTimeMillis()
                elapsedTime += currTimestamp - lastTimestamp
                lastTimestamp = currTimestamp
                elapsedTimeStr = MyDateUtil.formatTime(elapsedTime)
                delay(16L)
            }
        }
    }
}
