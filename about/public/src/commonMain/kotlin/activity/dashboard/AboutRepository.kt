package activity.dashboard

import kotlinx.coroutines.flow.StateFlow

interface AboutRepository {
    val showAbout: StateFlow<Boolean>

    fun toggleAbout(override: Boolean? = null)
}
