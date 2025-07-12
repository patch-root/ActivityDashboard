package activity.dashboard

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import me.tatarka.inject.annotations.Inject
import software.amazon.app.platform.scope.Scope
import software.amazon.app.platform.scope.Scoped
import software.amazon.app.platform.scope.coroutine.launch
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class SettingsRepositoryImpl(
    private val userManager: UserManager,
) : SettingsRepository, Scoped {
    private val _showSettings: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val showSettings: StateFlow<Boolean> = _showSettings.asStateFlow()

    override fun onEnterScope(scope: Scope) {
        scope.launch {
            // Close the settings page when there is no user.
            userManager.user.filter { it == null }.collect {
                _showSettings.value = false
            }
        }
    }

    override fun toggleSettings(override: Boolean?) {
        _showSettings.value = override ?: !_showSettings.value
    }
}
