package activity.dashboard

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AboutRepositoryImpl : AboutRepository {
    private val _showAbout: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val showAbout: StateFlow<Boolean> = _showAbout.asStateFlow()

    override fun toggleAbout(override: Boolean?) {
        _showAbout.value = override ?: !_showAbout.value
    }
}
