package activity.dashboard.fitbit

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@JsFun("key => new URLSearchParams(window.location.search).get(key)")
external fun getQueryParamJs(key: String): String?

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class WasmQueryParamProvider : QueryParamProvider {
    override fun getQueryParam(key: String): String? = getQueryParamJs(key)
}
