package activity.dashboard.fitbit

interface QueryParamProvider {
    fun getQueryParam(key: String): String?
}
