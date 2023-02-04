package dev.ky3he4ik.testtask.workouts

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chargemap.compose.numberpicker.NumberPicker
import dev.ky3he4ik.testtask.R
import dev.ky3he4ik.testtask.navigation.NavRoute
import dev.ky3he4ik.testtask.navigation.RouteNavigatorImpl
import dev.ky3he4ik.testtask.ui.components.ImageButton
import dev.ky3he4ik.testtask.ui.components.Pager
import dev.ky3he4ik.testtask.ui.components.rememberPagerState
import dev.ky3he4ik.testtask.util.MyDateUtil
import java.sql.Date
import kotlin.time.Duration


object WorkoutsRoute : NavRoute<WorkoutsViewModel> {
    override val route = "workouts/"

    @Composable
    override fun viewModel(): WorkoutsViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: WorkoutsViewModel) = WorkoutsScreen(viewModel)
}

@Composable
private fun WorkoutsScreen(viewModel: WorkoutsViewModel) {
    BackHandler(true) {
        // no back button
    }
    val pagerState = rememberPagerState()
    val workoutsCount = viewModel.viewState.workouts.size
    val contentModifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
        .clip(RoundedCornerShape(30.dp))
        .background(MaterialTheme.colors.background)
    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painterResource(id = R.drawable.background),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Pager(
                    contentModifier.weight(2f, false),
                    workoutsCount + 1, pagerState
                ) { page ->
                    if (page >= workoutsCount) {
                        AddWorkoutCard(viewModel)
                    } else {
                        ShowWorkoutCard(viewModel.viewState.workouts[page])
                    }
                }

                Stopwatch(
                    viewModel = viewModel,
                    modifier = contentModifier.weight(1f, false),
                )

                LazyColumn(
                    contentModifier.weight(0.5f, false).fillMaxWidth(),
                ) {
                    items(viewModel.viewState.savedTimeStr.size) {
                        Text(
                            text = viewModel.viewState.savedTimeStr[it],
                            fontSize = 20.sp,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddWorkoutCard(viewModel: WorkoutsViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        val workoutName = rememberSaveable { mutableStateOf("Exercise name") }
//                start: Timestamp, duration: Duration

        val hours = rememberSaveable { mutableStateOf(0) }
        val minutes = rememberSaveable { mutableStateOf(0) }
        val seconds = rememberSaveable { mutableStateOf(0) }

        val workoutStartDate = rememberSaveable {
            mutableStateOf(Date(System.currentTimeMillis()))
        }

        val today = MyDateUtil.today()
        val picker = DatePickerDialog(LocalContext.current, { _, year, month, day ->
            workoutStartDate.value = MyDateUtil.dayToDate(year, month, day)
        }, today.first, today.second, today.third)

        Text(
            text = "Add workout",
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        TextField(
            value = workoutName.value,
            onValueChange = { workoutName.value = it },
            textStyle = TextStyle(fontSize = 15.sp)
        )

        Button(onClick = { picker.show() }) {
            Text(
                MyDateUtil.dateToDay(workoutStartDate.value, LocalContext.current),
                fontSize = 15.sp
            )
        }

        Text(
            text = "Duration",
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberPicker(
                value = hours.value,
                range = 0..23,
                onValueChange = {
                    hours.value = it
                }
            )
            Text(":", fontSize = 10.sp)
            NumberPicker(
                value = minutes.value,
                range = 0..59,
                onValueChange = {
                    minutes.value = it
                }
            )
            Text(":", fontSize = 10.sp)
            NumberPicker(
                value = seconds.value,
                range = 0..59,
                onValueChange = {
                    seconds.value = it
                }
            )
        }

        Button(onClick = {
            viewModel.addWorkout(
                workoutStartDate.value,
                MyDateUtil.timeToDuration(
                    hours.value,
                    minutes.value,
                    seconds.value
                ),
                workoutName.value
            )
        }) {
            Text("Save", fontSize = 15.sp)
        }
    }
}

@Composable
private fun ShowWorkoutCard(workout: WorkoutsViewModel.Workout) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = MyDateUtil.dateToDay(
                workout.start,
                LocalContext.current,
            ),
            fontSize = 15.sp,
            textAlign = TextAlign.Start
        )
        Text(
            text = workout.duration.toString(),
            fontSize = 25.sp,
            textAlign = TextAlign.Center
        )
        Text(text = workout.name, fontSize = 30.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun Stopwatch(modifier: Modifier = Modifier, viewModel: WorkoutsViewModel) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = viewModel.elapsedTimeStr,
            fontSize = 30.sp,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (viewModel.viewState.isRunning) {
                ImageButton(
                    onClick = viewModel::stop,
                    resource = dev.ky3he4ik.testtask.R.drawable.ic_baseline_pause_24,
                    description = "Stop"
                )
            } else {
                ImageButton(
                    onClick = viewModel::start,
                    resource = dev.ky3he4ik.testtask.R.drawable.ic_baseline_play_arrow_24,
                    description = "Start"
                )
            }
            ImageButton(
                onClick = viewModel::reset,
                resource = dev.ky3he4ik.testtask.R.drawable.ic_baseline_replay_24,
                description = "Reset"
            )
            ImageButton(
                onClick = viewModel::nextLoop,
                resource = dev.ky3he4ik.testtask.R.drawable.ic_baseline_flag_24,
                description = "Loop"
            )
        }
    }
}

@Preview
@Composable
fun WorkoutPreview() {
    ShowWorkoutCard(
        WorkoutsViewModel.Workout(
            Date(1675431205000),
            Duration.parse("15m 5s"),
            "Kotlin programming"
        )
    )
}

@Preview
@Composable
fun AddWorkoutPreview() {
    AddWorkoutCard(
        WorkoutsViewModel(RouteNavigatorImpl())
    )
}
