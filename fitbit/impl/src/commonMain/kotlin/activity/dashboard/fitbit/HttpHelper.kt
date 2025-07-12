package activity.dashboard.fitbit

import activity.dashboard.common.logging.Logger

suspend inline fun <T> corsSafeCall(
    useProxy: Boolean,
    endpoint: String,
    crossinline block: suspend () -> T,
): FitbitResult<T> {
    return try {
        val result = block()
        FitbitResult.Success(result)
    } catch (e: Exception) {
        if (!useProxy) {
            Logger.error("Possible CORS issue when calling $endpoint directly")
            Logger.error("CORS restrictions may block Fitbit API requests. Try enabling the proxy.")
        }
        Logger.error("Exception during request to $endpoint: ${e.message}", e)
        FitbitResult.Error("Request to $endpoint failed: ${e.message}", e)
    }
}
