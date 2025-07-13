package activity.dashboard

import kotlinx.browser.window
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class WasmExternalEventHandler : ExternalEventHandler {
    override fun onBuyMeACoffeeClick() {
        window.open("https://buymeacoffee.com/patchroot", "_blank")
    }
}
