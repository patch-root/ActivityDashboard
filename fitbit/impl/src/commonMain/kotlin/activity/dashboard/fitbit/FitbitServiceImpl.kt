package activity.dashboard.fitbit

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class FitbitServiceImpl(
    private val auth: Auth,
    private val fitbitClient: FitbitClient,
) : FitbitService {
    override suspend fun getMonthlyActivityLogs(monthOffset: Int): FitbitResult<FitbitActivitiesResponse> {
        val token = auth.getValidAccessToken()
        return fitbitClient.getActivities(token, monthOffset)
    }

    override suspend fun getProfile(): FitbitResult<FitbitProfileResponse> {
        val token = auth.getValidAccessToken()
        return fitbitClient.getProfile(token)
    }
}
