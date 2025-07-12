package activity.dashboard.fitbit

import kotlinx.serialization.Serializable

@Serializable
data class ManualValuesSpecified(
    val calories: Boolean,
    val distance: Boolean,
    val steps: Boolean,
)
