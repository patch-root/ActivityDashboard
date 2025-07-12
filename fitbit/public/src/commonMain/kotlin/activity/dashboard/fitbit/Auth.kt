package activity.dashboard.fitbit

import kotlinx.coroutines.flow.StateFlow

interface Auth {
    companion object {
        const val REDIRECT = "https://activitydashboard.patchroot.com"
        const val CLIENT_ID = "23QK5T"
        const val ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
    }

    val accessToken: StateFlow<String?>

    suspend fun getValidAccessToken(): String

    fun clearFitbitAuth()

    fun buildRedirectUrl(): String

    fun retrieveCodeVerifier(): String

    fun login(url: String)
}
