package activity.dashboard.fitbit

import kotlinx.serialization.Serializable

@Serializable
data class ActivityLevel(
    val minutes: Int,
    val name: String,
)
