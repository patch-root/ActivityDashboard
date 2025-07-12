package activity.dashboard.fitbit

import kotlinx.serialization.Serializable

@Serializable
data class FitbitProfileResponse(
    val user: User,
)
