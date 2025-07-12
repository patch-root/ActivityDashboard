package activity.dashboard

import software.amazon.app.platform.scope.RootScopeProvider
import software.amazon.app.platform.scope.Scope
import software.amazon.app.platform.scope.coroutine.addCoroutineScopeScoped
import software.amazon.app.platform.scope.di.addDiComponent
import software.amazon.app.platform.scope.register

class Application : RootScopeProvider {
    private var _rootScope: Scope? = null

    override val rootScope: Scope
        get() = checkNotNull(_rootScope) { "Must call create() first." }

    fun create(appComponent: AppComponent) {
        check(_rootScope == null) { "create() should be called only once." }

        _rootScope =
            Scope.buildRootScope {
                addDiComponent(appComponent)

                addCoroutineScopeScoped(appComponent.appScopeCoroutineScopeScoped)
            }

        // Register instances after the rootScope has been set to avoid race conditions for Scoped
        // instances that may use the rootScope.
        rootScope.register(appComponent.appScopedInstances)
    }

    fun destroy() {
        rootScope.destroy()
        _rootScope = null
    }
}
