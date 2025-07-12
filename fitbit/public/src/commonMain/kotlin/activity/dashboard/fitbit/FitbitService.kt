package activity.dashboard.fitbit

interface FitbitService {
    suspend fun getMonthlyActivityLogs(monthOffset: Int): FitbitResult<FitbitActivitiesResponse>

    suspend fun getProfile(): FitbitResult<FitbitProfileResponse>
}
