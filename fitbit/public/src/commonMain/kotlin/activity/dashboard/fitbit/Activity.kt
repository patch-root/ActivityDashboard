package activity.dashboard.fitbit

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val activeDuration: Long,
    val activityLevel: List<ActivityLevel>,
    val activityName: String,
    val activityTypeId: Int,
    val calories: Int,
    val caloriesLink: String,
    val duration: Long,
    val elevationGain: Int,
    val lastModified: String,
    val logId: Long,
    val logType: String,
    val manualValuesSpecified: ManualValuesSpecified,
    val originalDuration: Long,
    val originalStartTime: String,
    val startTime: String,
    val steps: Int,
    val tcxLink: String,
)
