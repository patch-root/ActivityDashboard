package activity.dashboard

import activity.dashboard.NavigationHeaderPresenter.Model
import activity.dashboard.fitbit.Auth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class NavigationHeaderPresenterImpl(
    private val aboutRepository: AboutRepository,
    private val settingsRepository: SettingsRepository,
    private val userManager: UserManager,
    private val auth: Auth,
) : NavigationHeaderPresenter {
    @Composable
    override fun present(input: Unit): Model {
        var expanded by remember { mutableStateOf(false) }
        val user by userManager.user.collectAsState()

        return Model(
            user = user,
            expanded = expanded,
        ) {
            when (it) {
                NavigationHeaderPresenter.Event.ShowAbout -> aboutRepository.toggleAbout()
                NavigationHeaderPresenter.Event.ShowSettings -> settingsRepository.toggleSettings()
                is NavigationHeaderPresenter.Event.ToggleUserDropDown -> {
                    // Either toggle expanded drop down or set visibility.
                    expanded = it.override ?: !expanded
                }
                NavigationHeaderPresenter.Event.LogOut -> {
                    expanded = false
                    auth.clearFitbitAuth()
                }
                // This assumes that only the about menu can be "over" the home screen. When implementing the
                // Settings page, this will not be the case. I think that we should inverse this.
                NavigationHeaderPresenter.Event.NavigateHome -> aboutRepository.toggleAbout(false)
            }
        }
    }
}
