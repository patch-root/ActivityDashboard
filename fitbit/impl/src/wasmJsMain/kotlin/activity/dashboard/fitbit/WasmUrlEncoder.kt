package activity.dashboard.fitbit

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@JsFun("encodeURIComponent")
external fun encodeURIComponent(value: String): String

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class WasmUrlEncoder : UrlEncoder {
    override fun encode(value: String): String = encodeURIComponent(value)
}
