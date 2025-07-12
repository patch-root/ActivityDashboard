package activity.dashboard.fitbit

interface ProxySettings {
    fun save(enabled: Boolean)

    fun load(): Boolean
}
