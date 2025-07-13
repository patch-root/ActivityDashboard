package activity.dashboard

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import software.amazon.app.platform.scope.RootScopeProvider
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Component
@MergeComponent(scope = AppScope::class)
@SingleIn(AppScope::class)
abstract class WasmJsAppComponent(
    @get:Provides val rootScopeProvider: RootScopeProvider,
) :
    WasmJsAppComponentMerged {
    abstract val templateProviderFactory: TemplateProvider.Factory
    abstract val aboutRepository: AboutRepository
}
