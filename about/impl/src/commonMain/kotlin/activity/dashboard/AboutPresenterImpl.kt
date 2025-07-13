package activity.dashboard

import activity.dashboard.AboutPresenter.Model
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class AboutPresenterImpl(
    private val externalEventHandler: ExternalEventHandler,
) : AboutPresenter {
    @Composable
    override fun present(input: Unit): Model {
        return Model {
            when (it) {
                AboutPresenter.Event.Coffee -> externalEventHandler.onBuyMeACoffeeClick()
            }
        }
    }
}
