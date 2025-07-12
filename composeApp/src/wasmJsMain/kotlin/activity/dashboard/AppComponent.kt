package activity.dashboard

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.app.platform.scope.Scoped
import software.amazon.app.platform.scope.coroutine.CoroutineScopeScoped
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.ForScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
@SingleIn(AppScope::class)
interface AppComponent {
    @ForScope(AppScope::class)
    val appScopedInstances: Set<Scoped>

    @ForScope(AppScope::class)
    val appScopeCoroutineScopeScoped: CoroutineScopeScoped

    @Provides @IntoSet
    @ForScope(AppScope::class)
    fun provideEmptyScoped(): Scoped = Scoped.NO_OP
}
