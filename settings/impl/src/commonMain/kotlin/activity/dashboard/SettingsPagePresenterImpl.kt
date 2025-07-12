package activity.dashboard

import activity.dashboard.SettingsPagePresenter.Model
import activity.dashboard.fitbit.ProxySettings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class SettingsPagePresenterImpl(
    private val proxySettings: ProxySettings,
) : SettingsPagePresenter {
    @Composable
    override fun present(input: Unit): Model {
        var proxyEnabled by remember { mutableStateOf(proxySettings.load()) }

        return Model(
            proxyEnabled = proxyEnabled,
        ) {
            when (it) {
                is SettingsPagePresenter.Event.ToggleProxy -> {
                    proxyEnabled = it.enabled
                    proxySettings.save(proxyEnabled)
                }
            }
        }
    }
}
