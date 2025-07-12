package activity.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import me.tatarka.inject.annotations.Inject
import software.amazon.app.platform.presenter.BaseModel
import software.amazon.app.platform.scope.Scope
import software.amazon.app.platform.scope.di.diComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@Inject
@ContributesBinding(AppScope::class)
class NavigationDetailPresenterImpl(
    private val userManager: UserManager,
    private val loginPresenter: () -> LoginPresenter,
    private val aboutPresenter: AboutPresenter,
    private val aboutRepository: AboutRepository,
    private val settingsPagePresenter: SettingsPagePresenter,
    private val settingsRepository: SettingsRepository,
) : NavigationDetailPresenter {
    @Composable
    override fun present(input: Unit): BaseModel {
        val showAbout by aboutRepository.showAbout.collectAsState()
        val showSettings by settingsRepository.showSettings.collectAsState()

        // I need to figure out if I want to do it like this. Maybe these can be in a top level
        // "modal" presenter.
        if (showAbout) {
            return aboutPresenter.present(Unit)
        } else if (showSettings) {
            return settingsPagePresenter.present(Unit)
        }

        val scope = getUserScope()
        if (scope == null) {
            // If no user is logged in, then show the logged in screen.
            val loginPresenter = remember { loginPresenter() }
            return loginPresenter.present(Unit)
        }

        // A user is logged in. Use the user component to get an instance of UserPagePresenter, which is
        // only
        // part of the user scope.
        val userPresenter = remember(scope) { scope.diComponent<UserComponent>().userPresenter }
        return userPresenter.present(Unit)
    }

    @Composable
    private fun getUserScope(): Scope? {
        val user by userManager.user.collectAsState()
        return if (user?.scope?.isDestroyed() == true) null else user?.scope
    }

    /**
     * This component interface gives us access to objects from the user scope. We cannot inject
     * `UserPresenter` in the constructor, because it's part of the user scope.
     */
    @ContributesTo(UserScope::class)
    interface UserComponent {
        /** The [UserPagePresenter] provided by the user scope. */
        val userPresenter: UserPagePresenter
    }
}
