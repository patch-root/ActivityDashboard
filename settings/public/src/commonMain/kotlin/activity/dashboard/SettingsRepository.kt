package activity.dashboard

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val showSettings: StateFlow<Boolean>

    fun toggleSettings(override: Boolean? = null)
}
