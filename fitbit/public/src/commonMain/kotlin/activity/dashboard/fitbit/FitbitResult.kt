package activity.dashboard.fitbit

sealed class FitbitResult<out T> {
    data class Success<out T>(val data: T) : FitbitResult<T>()

    data class Error(val message: String, val cause: Throwable? = null) : FitbitResult<Nothing>()
}
