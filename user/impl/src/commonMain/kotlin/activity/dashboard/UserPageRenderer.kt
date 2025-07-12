package activity.dashboard

import activity.dashboard.UserPagePresenter.Event
import activity.dashboard.UserPagePresenter.Loadable
import activity.dashboard.UserPagePresenter.Model
import activity.dashboard.common.components.PillButton
import activity.dashboard.common.theme.ActivityDashboardTheme
import activity.dashboard.fitbit.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsBike
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Hiking
import androidx.compose.material.icons.outlined.Moving
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import software.amazon.app.platform.inject.ContributesRenderer
import software.amazon.app.platform.renderer.ComposeRenderer

@OptIn(ExperimentalSharedTransitionApi::class)
@ContributesRenderer
class UserPageRenderer : ComposeRenderer<Model>() {
    @Composable
    override fun Compose(model: Model) {
        ActivityDashboardTheme {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(24.dp),
            ) {
                when (val state = model.fitbitActivities) {
                    is Loadable.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is Loadable.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = state.message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PillButton(
                                    text = "Retry",
                                    onClick = { model.onEvent(Event.Retry) },
                                    backgroundColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                    is Loadable.Success -> {
                        ActivityListScreen(
                            activities = state.data.activities,
                            model = model,
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ActivityListScreen(
        activities: List<Activity>,
        model: Model,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val targetMonthDate = today.minus(DatePeriod(months = -model.currentMonthOffset))
            val activityMonthTitle =
                "${targetMonthDate.month.name.lowercase().replaceFirstChar(Char::uppercase)} ${targetMonthDate.year}"

            Text(
                text = "Activities for $activityMonthTitle",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PillButton(
                        text = "Previous Month",
                        onClick = { model.onEvent(Event.PreviousMonth) },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    )
                    if (model.currentMonthOffset < 0) {
                        PillButton(
                            text = "Next Month",
                            onClick = { model.onEvent(Event.NextMonth) },
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                }

                if (activities.isNotEmpty()) {
                    PillButton(
                        text = "Export",
                        onClick = { model.onEvent(Event.Export(activities)) },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            StyledActivityTable(activities)
        }
    }

    @Composable
    fun StyledActivityTable(activities: List<Activity>) {
        if (activities.isEmpty()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.EventBusy,
                        contentDescription = "No Activity",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier =
                            Modifier
                                .size(72.dp)
                                .padding(bottom = 16.dp),
                    )

                    Text(
                        text = "No activities yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = "There is no activity data available for this month.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            Box(
                modifier =
                    Modifier
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                ) {
                    LazyColumn {
                        stickyHeader {
                            TableHeader()
                        }
                        items(activities) { activity ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(animationSpec = tween(300)),
                            ) {
                                ActivityRow(
                                    activity = activity,
                                    modifier =
                                        Modifier
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TableHeader() {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                "Activity",
                modifier = Modifier.weight(2f, fill = true),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
            Text(
                "Date / Time",
                modifier = Modifier.weight(2f, fill = true),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
            Text(
                "Calories",
                modifier = Modifier.weight(1f, fill = true),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
            Text(
                "Duration",
                modifier = Modifier.weight(1f, fill = true),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
        )
    }

    @Composable
    fun ActivityRow(
        activity: Activity,
        modifier: Modifier = Modifier,
    ) {
        val parsedDate =
            try {
                Instant.parse(activity.startTime)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date.let {
                        "${it.month.name.lowercase().replaceFirstChar(Char::uppercase)} ${it.dayOfMonth}, ${it.year}"
                    }
            } catch (e: Exception) {
                "Unknown Date"
            }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = activityIcon(activity.activityName),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier =
                    Modifier
                        .size(28.dp)
                        .weight(0.5f, fill = true),
            )

            Text(
                activity.activityName,
                modifier = Modifier.weight(1.5f, fill = true),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                softWrap = true,
            )

            Text(
                parsedDate,
                modifier = Modifier.weight(2f, fill = true),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                softWrap = true,
            )

            Text(
                "${activity.calories}",
                modifier = Modifier.weight(1f, fill = true),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )

            Text(
                "${activity.duration / 60000} min",
                modifier = Modifier.weight(1f, fill = true),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                softWrap = true,
            )
        }
    }

    @Composable
    fun activityIcon(activityName: String): ImageVector {
        return when {
            activityName.contains("run", ignoreCase = true) -> Icons.AutoMirrored.Outlined.DirectionsRun
            activityName.contains("walk", ignoreCase = true) -> Icons.AutoMirrored.Outlined.DirectionsWalk
            activityName.contains("bike", ignoreCase = true) -> Icons.AutoMirrored.Outlined.DirectionsBike
            activityName.contains("swim", ignoreCase = true) -> Icons.Outlined.Pool
            activityName.contains("hike", ignoreCase = true) -> Icons.Outlined.Hiking
            activityName.contains("lift", ignoreCase = true) ||
                activityName.contains("gym", ignoreCase = true) -> Icons.Outlined.FitnessCenter
            else -> Icons.Outlined.Moving
        }
    }
}
