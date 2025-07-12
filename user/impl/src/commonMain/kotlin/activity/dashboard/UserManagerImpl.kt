package activity.dashboard

import activity.dashboard.common.logging.Logger
import activity.dashboard.fitbit.FitbitResult
import activity.dashboard.fitbit.FitbitService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.app.platform.scope.RootScopeProvider
import software.amazon.app.platform.scope.coroutine.addCoroutineScopeScoped
import software.amazon.app.platform.scope.di.addDiComponent
import software.amazon.app.platform.scope.register
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

/**
 * Production implementation of [UserManager].
 *
 * This class is responsible for creating the [UserScope] and [UserComponent].
 */
@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class UserManagerImpl(
    private val rootScopeProvider: RootScopeProvider,
    private val userComponentFactory: UserComponent.Factory,
    private val fitbitService: FitbitService,
) : UserManager {
    private val _user = MutableStateFlow<User?>(null)
    override val user: StateFlow<User?> = _user

    override suspend fun login(accessToken: String) {
        Logger.log("logging in")
        logout()

        when (val result = fitbitService.getProfile()) {
            is FitbitResult.Success -> {
                val fitbitUser = result.data.user

                val user =
                    UserImpl(
                        accessToken = accessToken,
                        name = fitbitUser.fullName,
                        profileImage = fitbitUser.avatar,
                    )

                val userComponent = userComponentFactory.createUserComponent(user)

                val userScope =
                    rootScopeProvider.rootScope.buildChild("user-$accessToken") {
                        addDiComponent(userComponent)
                        addCoroutineScopeScoped(userComponent.userScopeCoroutineScopeScoped)
                    }

                user.scope = userScope
                _user.value = user

                userScope.register(userComponent.userScopedInstances)
            }

            is FitbitResult.Error -> {
                // We are silently failing right now.
                Logger.error("Failed to load Fitbit profile during login: ${result.message}", result.cause)
            }
        }
    }

    override fun logout() {
        val currentUserScope = user.value?.scope

        _user.value = null

        currentUserScope?.destroy()
    }
}
