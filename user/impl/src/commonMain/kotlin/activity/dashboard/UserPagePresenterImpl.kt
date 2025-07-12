package activity.dashboard

import activity.dashboard.UserPagePresenter.Event
import activity.dashboard.UserPagePresenter.Loadable
import activity.dashboard.UserPagePresenter.Model
import activity.dashboard.common.logging.Logger
import activity.dashboard.fitbit.FitbitActivitiesResponse
import activity.dashboard.fitbit.FitbitResult
import activity.dashboard.fitbit.FitbitService
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(UserScope::class)
class UserPagePresenterImpl(
    private val fitBitService: FitbitService,
    private val pdfExporter: PdfExporter,
    private val user: User,
) : UserPagePresenter {
    @Composable
    override fun present(input: Unit): Model {
        var fitbitActivities by remember { mutableStateOf<Loadable<FitbitActivitiesResponse>>(Loadable.Loading) }
        var currentMonthOffset by remember { mutableStateOf(0) }
        val scope = rememberCoroutineScope()

        /**
         * Loads the Fitbit activity logs for the given month offset and updates UI state.
         *
         * This function is defined inside the `present()` Composable to ensure it has direct access
         * to local mutable state (`fitbitActivities`) and the current coroutine scope.
         *
         * While we could trigger recomposition via `LaunchedEffect` with a changing reload key,
         * explicitly calling this function in response to UI events, like retry, makes the control
         * flow clearer, avoids unnecessary recompositions, and prevents unbounded side-effect triggers.
         *
         * It handles:
         * - Setting the loading state
         * - Calling the Fitbit service
         * - Catching and reporting errors
         *
         * Used by:
         * - `LaunchedEffect` when the current month offset changes
         * - `Event.Retry` handler to reload the current month manually
         */
        fun loadActivitiesForMonth(offset: Int) {
            scope.launch {
                fitbitActivities = Loadable.Loading

                when (val result = fitBitService.getMonthlyActivityLogs(offset)) {
                    is FitbitResult.Success -> {
                        fitbitActivities = Loadable.Success(result.data)
                    }

                    is FitbitResult.Error -> {
                        val errorMessage = "Failed to load activities: ${result.message}"
                        Logger.error(errorMessage, result.cause)
                        fitbitActivities = Loadable.Error(errorMessage)
                    }
                }
            }
        }

        LaunchedEffect(currentMonthOffset) {
            loadActivitiesForMonth(currentMonthOffset)
        }

        return Model(
            fitbitActivities = fitbitActivities,
            currentMonthOffset = currentMonthOffset,
        ) {
            when (it) {
                is Event.Export ->
                    scope.launch {
                        pdfExporter.exportActivityPdf(
                            activities = it.activities,
                            name = user.name,
                            currentMonthOffset = currentMonthOffset,
                        )
                    }
                Event.NextMonth -> currentMonthOffset += 1
                Event.PreviousMonth -> currentMonthOffset -= 1
                Event.Retry -> loadActivitiesForMonth(currentMonthOffset)
            }
        }
    }
}
