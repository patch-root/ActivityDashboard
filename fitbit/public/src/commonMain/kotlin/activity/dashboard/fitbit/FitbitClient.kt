package activity.dashboard.fitbit

interface FitbitClient {
    suspend fun exchangeCodeForToken(
        code: String,
        codeVerifier: String,
    ): FitbitResult<FitbitResponse>

    suspend fun refreshToken(refreshToken: String): FitbitResult<FitbitResponse>

    suspend fun getActivities(
        accessToken: String,
        monthOffset: Int,
    ): FitbitResult<FitbitActivitiesResponse>

    suspend fun getProfile(accessToken: String): FitbitResult<FitbitProfileResponse>
}
