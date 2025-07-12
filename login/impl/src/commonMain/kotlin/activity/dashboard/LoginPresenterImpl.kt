package activity.dashboard

import activity.dashboard.LoginPresenter.Model
import activity.dashboard.fitbit.Auth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class LoginPresenterImpl(
    private val userManager: UserManager,
    private val auth: Auth,
) : LoginPresenter {
    @Composable
    override fun present(input: Unit): Model {
        var expandedImageUrl by remember { mutableStateOf<String?>(null) }
        var currentIndex by remember { mutableStateOf(0) }
        val accessToken by auth.accessToken.collectAsState()

        LaunchedEffect(Unit) {
            while (true) {
                delay(4000)
                currentIndex = (currentIndex + 1) % 3
            }
        }

        // Only build the redirect URL if the user is not logged in.
        val url =
            remember(accessToken) {
                if (accessToken == null) auth.buildRedirectUrl() else null
            }

        LaunchedEffect(accessToken) {
            if (accessToken != null) {
                userManager.login(accessToken!!)
            }
        }

        return Model(
            currentIndex = currentIndex,
            expandedImageUrl = expandedImageUrl,
        ) {
            when (it) {
                LoginPresenter.Event.Login -> {
                    url?.let { auth.login(url) }
                }
                is LoginPresenter.Event.SetImage -> {
                    expandedImageUrl = it.imageUrl
                }
            }
        }
    }
}
