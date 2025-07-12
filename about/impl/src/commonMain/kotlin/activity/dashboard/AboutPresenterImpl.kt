package activity.dashboard

import activity.dashboard.AboutPresenter.Model
import activity.dashboard.common.logging.Logger
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class AboutPresenterImpl(
    private val aboutRepository: AboutRepository,
) : AboutPresenter {
    @Composable
    override fun present(input: Unit): Model {
        val showAbout = remember { aboutRepository.showAbout.value }
        Logger.log("kellardw showAbout: $showAbout")

        return Model(url = "")
    }
}
