package activity.dashboard.fitbit

interface UrlEncoder {
    fun encode(value: String): String

    fun proxiedFitbitImageUrl(originalUrl: String): String {
        val encoded = encode(originalUrl)
        // TODO Should pull this base url out to share.
//        return "http://localhost:3000/api/fitbit/fitbit-image?url=$encoded"
        return "https://activitydashboard-proxy.patchroot.com/api/fitbit/fitbit-image?url=$encoded"
    }
}
