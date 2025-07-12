package activity.dashboard.fitbit

import kotlinx.serialization.Serializable

@Serializable
data class FitbitActivitiesResponse(
    val activities: List<Activity>,
    val pagination: Pagination,
)
