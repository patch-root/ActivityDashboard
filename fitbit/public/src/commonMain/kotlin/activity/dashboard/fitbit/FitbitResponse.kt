package activity.dashboard.fitbit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FitbitResponse(
    val scope: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("user_id")
    val userId: String,
)
