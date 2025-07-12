package activity.dashboard.fitbit

import kotlinx.browser.window
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class WasmProxySettings : ProxySettings {
    companion object {
        private const val KEY = "proxy_enabled"
    }

    override fun save(enabled: Boolean) {
        window.localStorage.setItem(KEY, enabled.toString())
    }

    override fun load(): Boolean {
        // default to true (use proxy).
        return window.localStorage.getItem(KEY)?.toBoolean() ?: true
    }
}
