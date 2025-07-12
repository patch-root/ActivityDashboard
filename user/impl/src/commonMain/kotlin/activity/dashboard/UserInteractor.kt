package activity.dashboard

import activity.dashboard.fitbit.Auth
import kotlinx.coroutines.flow.filter
import me.tatarka.inject.annotations.Inject
import software.amazon.app.platform.scope.Scope
import software.amazon.app.platform.scope.Scoped
import software.amazon.app.platform.scope.coroutine.launch
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(UserScope::class)
@ContributesBinding(UserScope::class)
class UserInteractor(
    private val userManager: UserManager,
    private val auth: Auth,
) : Scoped {
    override fun onEnterScope(scope: Scope) {
        scope.launch {
            auth.accessToken.filter { it == null }.collect {
                userManager.logout()
            }
        }
    }
}
